package org.netbeans.qms;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePathScanner;
import javax.swing.text.Document;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.Task;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.swing.Action;
import javax.swing.JPanel;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.JavaSource;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

public class QMSPanel extends JPanel implements ExplorerManager.Provider {

    private final ExplorerManager controler ;
    private final DataObject context;
    private final ArrayList<String> methodNames;

    public QMSPanel(DataObject context) {
        this.context = context;
        this.controler = new ExplorerManager();
        this.methodNames = new ArrayList<String>();
        setLayout(new BorderLayout());
        BeanTreeView btv = new BeanTreeView();
        btv.setRootVisible(false);
        add(btv, BorderLayout.CENTER);
        findMethods();
        controler.setRootContext(new RootNode());
        setPreferredSize(new Dimension(250,40+methodNames.size()*10));
    }

    private class MethodChildFactory extends ChildFactory<String> {
        @Override
        protected boolean createKeys(List<String> list) {
            list.addAll(methodNames);
            return true;
        }
        @Override
        protected Node createNodeForKey(String key) {
            BeanNode node=null;
            try {
                node = new BeanNode(key){
                    @Override
                    public Action getPreferredAction() {
                        return null;
                    }
                    @Override
                    public Action[] getActions(boolean context) {
                        return null;
                    }
                };
                node.setDisplayName(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
            return node;
        }
    }
    
    private class RootNode extends AbstractNode {
        public RootNode() {
            super(Children.create(new MethodChildFactory(), true));
        }
        
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return controler;
    }

    private void findMethods() {
        FileObject fileObject = context.getPrimaryFile();
        JavaSource javaSource = JavaSource.forFileObject(fileObject);
        if (javaSource != null) {
            try {
                javaSource.runUserActionTask(new Task<CompilationController>() {
                    @Override
                    public void run(CompilationController compilationController) throws Exception {
                        compilationController.toPhase(Phase.ELEMENTS_RESOLVED);
                        Document document = compilationController.getDocument();
                        if (document != null) {
                            new MemberVisitor(compilationController).scan(compilationController.getCompilationUnit(), null);
                        }
                    }
                }, true);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private class MemberVisitor extends TreePathScanner<Void, Void> {
        private final CompilationInfo info;
        public MemberVisitor(CompilationInfo info) {
            this.info = info;
        }
        @Override
        public Void visitClass(ClassTree t, Void v) {
            Element el = info.getTrees().getElement(getCurrentPath());
            if (el != null) {
                TypeElement te = (TypeElement) el;
                java.util.List<? extends Element> enclosedElements = te.getEnclosedElements();
                for (int i = 0; i < enclosedElements.size(); i++) {
                    Element enclosedElement = (Element) enclosedElements.get(i);
                    if (enclosedElement.getKind() == ElementKind.METHOD) {
                        methodNames.add(enclosedElement.getSimpleName().toString());
                    }
                }
            }
            return null;
        }
    }

}
