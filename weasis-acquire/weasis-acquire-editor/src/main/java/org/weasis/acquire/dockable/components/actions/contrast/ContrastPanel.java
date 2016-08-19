package org.weasis.acquire.dockable.components.actions.contrast;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.weasis.acquire.AcquireObject;
import org.weasis.acquire.dockable.components.actions.AbstractAcquireActionPanel;
import org.weasis.acquire.dockable.components.actions.contrast.comp.BrightnessComponent;
import org.weasis.acquire.dockable.components.actions.contrast.comp.ContrastComponent;
import org.weasis.acquire.dockable.components.util.AbstractComponent;
import org.weasis.acquire.dockable.components.util.AbstractSliderComponent;
import org.weasis.acquire.explorer.AcquireImageInfo;
import org.weasis.acquire.explorer.AcquireImageValues;
import org.weasis.acquire.operations.OpValueChanged;
import org.weasis.acquire.operations.impl.AutoLevelListener;
import org.weasis.base.viewer2d.EventManager;
import org.weasis.core.api.image.BrightnessOp;
import org.weasis.core.api.image.ImageOpNode;
import org.weasis.core.api.media.data.ImageElement;
import org.weasis.core.ui.editor.image.ViewCanvas;

public class ContrastPanel extends AbstractAcquireActionPanel implements ChangeListener, OpValueChanged {
    private static final long serialVersionUID = -3978989511436089997L;

    private final AbstractSliderComponent contrastPanel;
    private final AbstractSliderComponent brightnessPanel;
    private final AutoLevelListener autoLevelListener;

    private JCheckBox autoLevelBtn = new JCheckBox("Auto Level");

    public ContrastPanel() {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        autoLevelListener = new AutoLevelListener();
        autoLevelBtn.addActionListener(autoLevelListener);
        contrastPanel = new ContrastComponent(this);
        brightnessPanel = new BrightnessComponent(this);

        JPanel content = new JPanel(new GridLayout(3, 1, 0, 10));
        content.add(contrastPanel);
        content.add(brightnessPanel);
        content.add(autoLevelBtn);

        add(content, BorderLayout.NORTH);
    }

    @Override
    public boolean needValidationPanel() {
        return true;
    }

    @Override
    public void initValues(AcquireImageInfo info, AcquireImageValues values) {
        AcquireImageValues next = info.getNextValues();
        next.setContrast(values.getContrast());
        next.setBrightness(values.getBrightness());
        next.setAutoLevel(values.isAutoLevel());

        autoLevelBtn.removeActionListener(autoLevelListener);
        contrastPanel.removeChangeListener(this);
        brightnessPanel.removeChangeListener(this);
        contrastPanel.setSliderValue(next.getContrast());
        brightnessPanel.setSliderValue(next.getBrightness());
        contrastPanel.addChangeListener(this);
        brightnessPanel.addChangeListener(this);
        autoLevelBtn.setSelected(next.isAutoLevel());
        repaint();

        applyNextValues();
        autoLevelListener.applyNextValues();
        
        ViewCanvas<ImageElement> view = EventManager.getInstance().getSelectedViewPane();
        info.applyPreProcess(view);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        JPanel panel = (JPanel) slider.getParent();
        if (panel instanceof AbstractSliderComponent) {
            ((AbstractComponent) panel).updatePanelTitle();
        }

        AcquireImageInfo imageInfo = AcquireObject.getImageInfo();
        imageInfo.getNextValues().setBrightness(brightnessPanel.getSliderValue());
        imageInfo.getNextValues().setContrast(contrastPanel.getSliderValue());
        applyNextValues();
        imageInfo.applyPreProcess(AcquireObject.getView());
    }

    @Override
    public void applyNextValues() {
        AcquireImageInfo imageInfo = AcquireObject.getImageInfo();
        ImageOpNode node = imageInfo.getPreProcessOpManager().getNode(BrightnessOp.OP_NAME);
        if (node == null) {
            node = new BrightnessOp();
            imageInfo.addPreProcessImageOperationAction(node);
        } else {
            node.clearIOCache();
        }
        node.setParam(BrightnessOp.P_BRIGTNESS_VALUE, (double) imageInfo.getNextValues().getBrightness());
        node.setParam(BrightnessOp.P_CONTRAST_VALUE, (double) imageInfo.getNextValues().getContrast());
    }
}
