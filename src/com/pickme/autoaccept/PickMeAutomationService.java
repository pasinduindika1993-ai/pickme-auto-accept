package com.pickme.autoaccept;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.util.Log;
import java.util.List;

public class PickMeAutomationService extends AccessibilityService {
    private static final String TAG = "PickMeAutoAccept";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        checkAndAccept(root);
        root.recycle();
    }

    private void checkAndAccept(AccessibilityNodeInfo node) {
        if (node == null) return;
        List<AccessibilityNodeInfo> nodes = node.findAccessibilityNodeInfosByText("ACCEPT");
        if (nodes != null) {
            for (AccessibilityNodeInfo n : nodes) {
                if (n != null && n.isClickable()) {
                    n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                if (n != null) n.recycle();
            }
        }
    }

    @Override
    public void onInterrupt() {}

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
