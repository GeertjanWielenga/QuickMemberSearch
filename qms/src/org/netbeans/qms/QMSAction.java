package org.netbeans.qms;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Edit",
        id = "org.netbeans.qms.QMSAction"
)
@ActionRegistration(
        displayName = "#CTL_QMSAction"
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 400, separatorAfter = 450),
    @ActionReference(path = "Shortcuts", name = "O-Q")}
)
@Messages("CTL_QMSAction=Quick Member Search")
public final class QMSAction implements ActionListener {

    private final DataObject context;

    public QMSAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Mode floater = WindowManager.getDefault().findMode("undockedNavigator");
        Object oldTabDisplayer = UIManager.get("EditorTabDisplayerUI");
        UIManager.put("EditorTabDisplayerUI", "org.netbeans.qms.NoTabsTabDisplayerUI");
        TopComponent tc = WindowManager.getDefault().findTopComponent("navigatorTC");
        floater.dockInto(tc);
        tc.open();
        JFrame root = (JFrame) SwingUtilities.getRoot(tc);
        root.setExtendedState(root.getExtendedState() | Frame.MAXIMIZED_BOTH);
        root.setPreferredSize(new Dimension(262,355));
        root.setLocation(810, 231);
        root.pack();
        root.setAlwaysOnTop(true);
        UIManager.put("EditorTabDisplayerUI", oldTabDisplayer);
    }

}
