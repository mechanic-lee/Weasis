package org.weasis.acquire.dockable.components;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.UIManager;

import org.weasis.acquire.dockable.components.actions.AcquireAction;
import org.weasis.acquire.dockable.components.actions.AcquireAction.Cmd;

public class AcquireSubmitButtonsPanel extends JPanel {
    private static final long serialVersionUID = 4890844708371941062L;
    
    private final AcquireActionButton cancelBtn;
    private final AcquireActionButton resetBtn;
    
    public AcquireSubmitButtonsPanel() {
        setBorder(UIManager.getBorder("TitledBorder.border"));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 10, 10);
        setLayout(flowLayout);
        
        cancelBtn = new AcquireActionButton("Cancel", Cmd.CANCEL);
        cancelBtn.setToolTipText("Return to previous values");
        resetBtn = new AcquireActionButton("Reset", Cmd.RESET);
        resetBtn.setToolTipText("Reset to default values");
        
        add(cancelBtn);
        add(resetBtn);
    }

    public void setAcquireAction(AcquireAction acquireAction) {
        cancelBtn.setAcquireAction(acquireAction);
        resetBtn.setAcquireAction(acquireAction);
    }
}
