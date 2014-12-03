package org.netbeans.qms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.*;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

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
//        DialogDescriptor dd = new DialogDescriptor(new QMSPanel(context), "Quick Member Search");
//        DialogDisplayer.getDefault().createDialog(dd).setVisible(true);
    }

}
