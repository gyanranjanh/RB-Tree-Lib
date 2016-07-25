/* The name of this project was kept Rbst initially 
 * to convey a Red-Black search tree. But the project
 * submission requirement is that we need to name
 * the executable as 'bbst'. It is only possible if
 * we rename the class name itself as bbst. It does
 * not follow java standard naming convention but
 * it kept so just to meet the submission requirement */

import java.util.*;
import java.io.*;
import java.io.File;


public class bbst {

    private static int noOfElements    = 0;
    private static final boolean RED   = true;
    private static final boolean BLACK = false;
    private RbstNode root; // root of rbst tree

    private static final int Rb0       = 0; // case identifier Rb0
    private static final int Rb1       = 1; // case identifier Rb1
    private static final int Rb2       = 2; // case identifier Rb2
    private static final int Rr0       = 0; // case identifier Rr0
    private static final int Rr1       = 1; // case identifier Rr1
    private static final int Rr2       = 2; // case identifier Rr2

    private static final int Lb0       = 0; // case identifier Lb0
    private static final int Lb1       = 1; // case identifier Lb1
    private static final int Lb2       = 2; // case identifier Lb2
    private static final int Lr0       = 0; // case identifier Lr0
    private static final int Lr1       = 1; // case identifier Lr1
    private static final int Lr2       = 2; // case identifier Lr2

    public static class RbstNode implements Cloneable {
        int key_id;
        int count;
        boolean color;
        RbstNode parent;
        RbstNode left;
        RbstNode right;

        public RbstNode() { }

        // ...
        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public bbst() { root = null; }

    public bbst(RbstNode []rbstNodes) { 
        root = null;

        RbstUtil.rbst_log("Max level is: " + (int)(Math.log(rbstNodes.length)/Math.log(2) + 1));

        root = rbst_create(rbstNodes, 0, rbstNodes.length, 1,
                                (int)(Math.log(rbstNodes.length)/Math.log(2) + 1));
    }

    public bbst(int []ip) { 
        root = null;
        rbst_create(ip);
    }

    public RbstNode rbst_get_root() {
        return root;
    }

    /* create rbst with input array 'ip' 
     * create rbst in O(nlogn) time*/
    public boolean rbst_create(int []ip) {
        // create Rbst
        // key_id(i) = ip[i] count(i) = ip[i++], i = 0, 2, 4,....n-1
        if(root == null) {
            for(int i = 0; i < ip.length - 1; i++) {
                //create node with next 'key_id' and 'count'
                RbstNode node = new RbstNode();
                // save the node in tree's arraylist
                node.key_id = ip[i++];
                node.count  = ip[i];
                //initialize to BLACK
                node.color  = BLACK;
                //initialize left and right child pointer
                node.parent = node.left = node.right = null;
                if(!this.rbst_insert(node)) {
                    RbstUtil.rbst_log("rbst_create>" + "failed to create tree");
                    break;
                }
            }
        }

        return true;
    }

    /* create rbst with input array 'rbstNodes'.
     * create rbst in O(n) time*/
    private RbstNode rbst_create(RbstNode []rbstNodes, int start,
                                int n, int level, int maxLevel) {
        if(start >= n) {
            return null;
        }
        RbstNode tree  = rbstNodes[(start + n - 1)/2];
        RbstNode left  = rbst_create(rbstNodes, start, (start + n - 1)/2,
                                                            level + 1, maxLevel);
        RbstNode right = rbst_create(rbstNodes, (start + n - 1)/2 + 1, n,
                                                            level + 1, maxLevel);


        /* set the color of the node to RED
         * if it is in the bottom level of
         * complete binary tree */
        if((left == null && right == null)
            && (level == maxLevel)) {
            tree.color = RED;
        }

        if(left != null) {
            tree.left   = left;
            left.parent = tree;
        }
        if(right != null) {
            tree.right   = right;
            right.parent = tree;
        }

        return tree;
    }

    /* insert 'node_z' to rbst */
    public boolean rbst_insert(RbstNode node_z) {
        boolean result  = true;
        RbstNode node_y = null;
        RbstNode node_x = root;

        while(node_x != null) {
            node_y = node_x;
            if (node_z.key_id < node_x.key_id) {
                node_x = node_x.left;
            }
            else {
                node_x = node_x.right;
            }
        }

        node_z.parent = node_y;

        if(node_y == null) {
            RbstUtil.rbst_log("inserting root");
            root = node_z;
        }
        else if(node_z.key_id < node_y.key_id) {
            node_y.left = node_z;
        }
        else {
            node_y.right = node_z;
        }

        node_z.color = RED;

        /* readjustment after insertion */
        this.rbst_insert_fix_up(node_z);

        return result;
    }

    /* readjustment after insertion of tree rooted
     * at 'node_z'. readjustment is done from 'node_z' 
     * upwards till root node */
    private void rbst_insert_fix_up(RbstNode node_z) {
        RbstNode node_gp = null, node_p = node_z.parent;
        RbstNode node_d  = null; // right child of gp

        while ((node_p != null) 
               && (node_p.parent != null) 
               && (node_p.color == RED)) {
            node_gp = node_p.parent;
            if(node_p == node_gp.left) {
                node_d  = node_gp.right;
            }
            else {
                node_d  = node_gp.left;
            }
            if(node_d != null && node_d.color == RED) {
                /* XYr */
                /* Remedy: color flip */
                RbstUtil.rbst_log("insert case: XYr");
                node_d.color  = BLACK;
                node_gp.color = RED;
                node_p.color  = BLACK;
                node_z        = node_gp;
                node_p        = node_z.parent;
            }
            else if(node_p == node_gp.left){
                /* LYb */
                if(node_z == node_p.left) {
                    /* LLb */
                    /* Remedy: LL = Right or clock-wise rotation */
                    RbstUtil.rbst_log("insert case: LLb");
                    node_gp.color = RED;
                    node_p.color  = BLACK;
                    this.rbst_right_rotate(node_gp);
                    // Done
                    node_p = null;
                }
                else {
                    /* LRb */
                    /* Remedy: LR = RR + LL rotation
                     *            = left followed by right rotation */
                     RbstUtil.rbst_log("insert case: LRb");
                     node_gp.color = RED;
                     node_z.color  = BLACK;
                     this.rbst_left_rotate(node_p);
                     this.rbst_right_rotate(node_gp);
                     // Done!
                     node_p = null;
                }
            }
            else {
                /* RYb */
                if(node_z == node_p.left) {
                    /* RLb */
                    /* Remedy: RL = LL + RR rotation
                     *            = right followed by left rotation */
                     RbstUtil.rbst_log("insert case: RLb");
                     node_gp.color = RED;
                     node_z.color  = BLACK;
                     this.rbst_right_rotate(node_p);
                     this.rbst_left_rotate(node_gp);
                     // Done!
                     node_p = null;
                }
                else {
                    /* RRb */
                    /* Remedy: RR = Left or counter-clockwise rotation */
                    RbstUtil.rbst_log("insert case: RRb");
                    node_gp.color = RED;
                    node_p.color  = BLACK;
                    this.rbst_left_rotate(node_gp);
                    // Done!
                    node_p = null;
                }
            }
        }

        root.color = BLACK;
    }

    /* increase the count value if node with 'key_id' found.
     * increase the value by 'm' */
    public boolean rbst_increase(int key_id, int m) {
        RbstNode node = null;
        boolean result = true;

        // 1. increase the count value if node with 'key_id' found
        // 2. if node not found then return false

        if((node = rbst_find(key_id)) != null) {
            if(node.key_id == key_id) {
                // increase count value 
                if(m >= 0) {
                    node.count += m;
                    RbstUtil.rbst_log_final(node.count);
                }
                else {
                    RbstUtil.rbst_log("invalid increment calue: " + m);
                }
            }
            else
            {
                // node not found in rbst
                // insert node into rbst
                RbstUtil.rbst_log("node not found");
                RbstUtil.rbst_log("inserting...");

                node = new RbstNode();
                node.key_id = key_id;
                node.count  = m;
                node.color  = BLACK;
                node.parent = node.left = node.right = null;

                result = rbst_insert(node);

                if(result == true) {
                    RbstUtil.rbst_log_final(node.count);
                }
            }
        }
        else {
            result = false;
        }

        return result;
    }

    /* delete node with key 'key_id' from rbst */
    public boolean rbst_delete(int key_id) throws Exception {
        boolean result = true;
        RbstNode node_z  = null;

        if (root != null) {
            if((node_z = rbst_find(key_id)) != null) {
                if(node_z != null && key_id == node_z.key_id) {
                    /* node with 'key_id' found */
                    if(node_z.left != null && node_z.right != null) {
                        RbstNode pred = null;

                        /* redefine the problem by replacing the
                         * target node with its predecessor */
                        /* predecessor is the largest node in the
                         * left sub-tree */
                        pred = rbst_find_predecessor(node_z);

                        if(pred != null) {

                            // swap position of node_z and pred in rbst
                            rbst_swap_nodes(node_z, pred);

                            // Now, we are ready to delete node_z as an 
                            // ordinary deletion
                        }
                        else {
                            // error - no predecessor found
                            RbstUtil.rbst_log("no predecessor found");
                            result = false;
                        }
                    }

                    // Delete node_z

                    if(node_z.color == RED) {
                        /* if it is a red leaf node then delete the node -
                         * no adjustment required */
                        rbst_delete_node(node_z);
                    }
                    else {
                        // 1. node_z is not a red node
                        // 2. delete this black node
                        // 3. readjustment is needed in case basis

                        // Mark the root of the deficient subtree
                        RbstNode node_y  = null;
                        RbstNode node_py = null;

                        /* we already know that node_z can't have
                         * 2 children at this point. we ahve han-
                         * dled that case earlier above */
                        if(node_z.left != null) {
                            node_y  = node_z.left;
                            node_py = node_z.parent;
                        }
                        else if(node_z.right != null){
                            node_y  = node_z.right;
                            node_py = node_z.parent;
                        }
                        else {
                            node_py = node_z.parent;
                        }
                        // delete black node
                        rbst_delete_node(node_z);

                        if(node_y != null) {
                            RbstUtil.rbst_log("node_y: " + node_y.key_id + 
                            node_y.color);
                        }
                        else {
                            RbstUtil.rbst_log("node_y: null");
                        }

                        if(node_py != null) {
                            RbstUtil.rbst_log("node_py: " + node_py.key_id);
                        }
                        else {
                            RbstUtil.rbst_log("node_py: null");
                        }
                        // readjust rbst if necessary
                        rbst_delete_fix_up(node_y, node_py);
                    } //if (node_z.color == RED)
                }
                else {
                    RbstUtil.rbst_log("rbst_delete> " + "node not found with" + 
                                                                " id: " + key_id);
                }
            }
            else {
                RbstUtil.rbst_log("rbst_delete> " + "node not found with id: " + key_id);
                result = false;
            }
        }
        else {
            RbstUtil.rbst_log("Rbst does not exist");
        }

        return result;
    }

    /* remove deficiency of tree rooted at 'node_y'.
     * deficiency is removed through repeated 
     * readjustment done from 'node_y' towards root */
    private void rbst_delete_fix_up(RbstNode node_y, RbstNode node_py) {

        while(node_py != null) {
            if(node_y != null && node_y.color == RED) {
                // 1. If y is a red node, make it black
                node_y.color = BLACK;
                // 2. Now, no subtree is deficient.  Done!
                node_py = null;
            }
            else {
                if(node_py != null) {
                    // 1. y is black but not the root (there is a py).
                    // 2. Identify the cases.
                    // 3. Make appropriate readjustment

                    if(node_y == node_py.right) {
                        // 1. node_y is the right child of node_py
                        // 2. case: Rcn
                        RbstNode node_v = node_py.left;

                        if(node_v.color == BLACK) {
                            // case Rbn
                            int n = 0; // no of red children of node_v

                            // Identify n in Rbn
                            n = ((node_v.left != null) 
                                && (node_v.left.color == RED)) ? ++n : 0;
                            n = ((node_v.right != null) 
                                && (node_v.right.color == RED)) ? ++n : 0;

                            switch(n) {
                                case Rb0:
                                {
                                    // 1. case Rb0
                                    // 2. identify case 1 or case 2
                                    if(node_py.color == BLACK) {
                                        // 3. case 1: node py is black
                                        // 4. Remedy: color change
                                        RbstUtil.rbst_log("case: Rb0-case1");
                                        node_v.color = RED;
                                        // 5. Now, py is root of deficient subtree.
                                        // 6. Continue!
                                        node_y  = node_py;
                                        node_py = node_y.parent;
                                    }
                                    else {
                                        // 3. case 2: node py is red
                                        // 4. Remedy: color flip
                                        node_py.color = BLACK;
                                        node_v.color  = RED;
                                        RbstUtil.rbst_log("case: Rb0-case2");
                                        // 5. Deficiency eliminated.
                                        // 6. Done!
                                        node_py = null;
                                    }
                                }
                                break;
                                case Rb1:
                                {
                                    // 1. case Rb1
                                    // 2. identify case 1 or case 2
                                    if((node_v.left != null) && node_v.left.color == RED) {
                                        /* 3. case 1 : node v's left child is 
                                         *    red */
                                        // 4. Remedy : LL rotation
                                        // 5.         = right-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rb1-case1");
                                        node_v.left.color = BLACK;
                                        node_v.color = node_py.color;
                                        rbst_right_rotate(node_py);
                                        // 6. Done!
                                    }
                                    else {
                                        /* 3. case 2 : node v's right child is 
                                         *    red */
                                        // 4. Remedy: LR rotation(RR + LL).
                                        //          = left-rotation(node_v) +
                                        //            right-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rb1-case2");
                                        RbstNode node_w = node_v.right; // v's right child w
                                        node_w.color = node_py.color;
                                        rbst_left_rotate(node_v);
                                        rbst_right_rotate(node_py);
                                        // 5. Deficiency eliminated.
                                        // 6. Done!
                                    }
                                    node_py = null;
                                }
                                break;
                                case Rb2:
                                {
                                    // 1. case Rb2
                                    // 2. Remedy: LR rotation.
                                    //          = left-rotation(node_v) +
                                    //            right-rotation(node_py)
                                    RbstUtil.rbst_log("case: Rb2");
                                    RbstNode node_w = node_v.right; // v's right child w
                                    node_w.color = node_py.color;
                                    rbst_left_rotate(node_v);
                                    rbst_right_rotate(node_py);
                                    // 3. Deficiency eliminated.
                                    // 4. Done!
                                    node_py = null;
                                }
                                break;
                                default:
                                {
                                    RbstUtil.rbst_log("Unhandled case:" 
                                    + " Rb" + n);
                                    RbstUtil.rbst_log("rbst_delete_fix_up:<\n");
                                    return;
                                }
                            }
                        }
                        else {
                            // case Rrn
                            int n = 0; // # of red children of v's right child w
                            RbstNode node_w = node_v.right; // v's right child w

                            // Identify n in Rbn
                            if(node_w != null) {
                                n = (node_w.left.color == RED) ? ++n : 0;
                                n = (node_w.right.color == RED) ? ++n : 0;
                            }

                            switch(n) {
                                case Rr0:
                                {
                                    // 1. case Rr0
                                    // 2. Remedy : LL rotation
                                    // 3.         = right-rotation(node_py)
                                    RbstUtil.rbst_log("case: Rr0");
                                    node_v.color = BLACK;
                                    if(node_v.right != null) {
                                        node_v.right.color = RED;
                                    }
                                    rbst_right_rotate(node_py);
                                    // 4. Done
                                }
                                break;
                                case Rr1:
                                {
                                    // case Rr1
                                    // identify case 1 or case 2
                                    if((node_w.left != null) && node_w.left.color == RED) {
                                        // 1. case 1
                                        // 2. Remedy: LR rotation.
                                        //          = left-rotation(node_v) +
                                        //            right-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rr1 - case1");
                                        node_w.left.color = BLACK;
                                        rbst_left_rotate(node_v);
                                        rbst_right_rotate(node_py);
                                        // 3. Deficiency eliminated.
                                        // 4. Done!
                                    }
                                    else {
                                        // 1. case 2
                                        // 2. Remedy: left-rotation(node_w) +
                                        //            left-rotation(node_v) +
                                        //            right-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rr1 - case2");
                                        node_w.right.color = BLACK;
                                        rbst_left_rotate(node_w);
                                        rbst_left_rotate(node_v);
                                        rbst_right_rotate(node_py);
                                        // 3. Deficiency eliminated.
                                        // 4. Done!
                                    }
                                }
                                break;
                                case Rr2:
                                {
                                    // 1. case Rr2
                                    // 2. Remedy: left-rotation(node_w) +
                                    //            left-rotation(node_v) +
                                    //            right-rotation(node_py)
                                    RbstUtil.rbst_log("case:Rr2");
                                    node_w.right.color = BLACK;
                                    rbst_left_rotate(node_w);
                                    rbst_left_rotate(node_v);
                                    rbst_right_rotate(node_py);
                                    // 3. Deficiency eliminated.
                                    // 4. Done!
                                }
                                break;
                                default:
                                {
                                    RbstUtil.rbst_log("Unhandled case:" 
                                    + " Rr" + n);
                                    RbstUtil.rbst_log("rbst_delete_fix_up:<\n");
                                    return;
                                }
                            }
                            node_py = null;
                        } // case Rrn
                    } // case Rcn
                    else {
                        // 1. node_y is the left child of node_py
                        // 2. case: Lcn
                        // 3. mirror handling of case Rcn
                        RbstNode node_v = node_py.right;

                        if(node_v.color == BLACK) {
                            // case Rbn
                            int n = 0; // no of red children of node_v

                            // Identify n in Rbn
                            n = ((node_v.left != null) 
                                && (node_v.left.color == RED)) ? ++n : 0;
                            n = ((node_v.right != null) 
                                && (node_v.right.color == RED)) ? ++n : 0;

                            if((node_v.left != null)) {
                                RbstUtil.rbst_log("case: x " + node_v.left.color
                                                    + " n: " + n);
                            }

                            if((node_v.right != null)) {
                                RbstUtil.rbst_log("case: y " + node_v.right.color
                                                    + " n: " + n);
                            }

                            switch(n) {
                                case Lb0:
                                {
                                    // 1. case Lb0
                                    // 2. identify case 1 or case 2
                                    if(node_py.color == BLACK) {
                                        // 3. case 1: node py is black
                                        // 4. Remedy: color change
                                        RbstUtil.rbst_log("case: Lb0-case1");
                                        node_v.color = RED;
                                        // 5. Now, py is root of deficient subtree.
                                        // 6. Continue!
                                        node_y  = node_py;
                                        node_py = node_y.parent;
                                    }
                                    else {
                                        // 3. case 2: node py is red
                                        // 4. Remedy: color flip
                                        node_py.color = BLACK;
                                        node_v.color  = RED;
                                        RbstUtil.rbst_log("case: Lb0-case2");
                                        // 5. Deficiency eliminated.
                                        // 6. Done!
                                        node_py = null;
                                    }
                                } // case Lb0
                                break;
                                case Lb1:
                                {
                                    // 1. case Lb1
                                    // 2. identify case 1 or case 2
                                    if((node_v.right != null)&& node_v.right.color == RED) {
                                        /* 3. case 1 : node v's left child is 
                                         *    red */
                                        // 4. Remedy : RR rotation
                                        // 5.         = right-rotation(node_py)
                                        RbstUtil.rbst_log("case: Lb1-case1");
                                        node_v.right.color = BLACK;
                                        node_v.color = node_py.color;
                                        rbst_left_rotate(node_py);
                                        // 6. Done!
                                    }
                                    else {
                                        /* 3. case 2 : node v's right child is 
                                         *    red */
                                        // 4. Remedy: RL rotation.
                                        //          = right-rotation(node_v) +
                                        //            left-rotation(node_py)
                                        RbstUtil.rbst_log("case: Lb1-case2");
                                        RbstNode node_w = node_v.left; // v's left child w
                                        node_w.color = node_py.color;
                                        rbst_right_rotate(node_v);
                                        rbst_left_rotate(node_py);
                                        // 5. Deficiency eliminated.
                                        // 6. Done!
                                    }
                                    node_py = null;
                                } // case Lb1
                                break;
                                case Lb2:
                                {
                                    // 1. case Lb2
                                    // 2. Remedy: RL rotation.
                                    //          = right-rotation(node_v) +
                                    //            left-rotation(node_py)
                                    RbstUtil.rbst_log("case: Rb2");
                                    RbstNode node_w = node_v.left; // v's left child w
                                    node_w.color = node_py.color;
                                    rbst_right_rotate(node_v);
                                    rbst_left_rotate(node_py);
                                    // 3. Deficiency eliminated.
                                    // 4. Done!
                                    node_py = null;
                                } // case Lb2
                                break;
                                default:
                                {
                                    RbstUtil.rbst_log("Unhandled case:" 
                                    + " Rb" + n);
                                    RbstUtil.rbst_log("rbst_delete_fix_up:<\n");
                                    return;
                                }
                            }
                        } // case Lbn
                        else {
                            // case Lrn
                            int n = 0; // # of red children of v's left child w
                            RbstNode node_w = node_v.left; // v's left child w

                            // Identify n in Rbn
                            if(node_w != null) {
                                n = (node_w.left.color == RED) ? ++n : 0;
                                n = (node_w.right.color == RED) ? ++n : 0;
                            }

                            switch(n) {
                                case Lr0:
                                {
                                    // 1. case Lr0
                                    // 2. Remedy : RR rotation
                                    // 3.         = right-rotation(node_py)
                                    RbstUtil.rbst_log("case: Lr0");
                                    node_v.color = BLACK;
                                    if(node_v.left != null) {
                                        node_v.left.color = RED;
                                    }
                                    rbst_left_rotate(node_py);
                                    // 4. Done
                                } // case Lr0
                                break;
                                case Lr1:
                                {
                                    // case Lr1
                                    // identify case 1 or case 2
                                    if((node_w.right != null) && node_w.right.color == RED) {
                                        // 1. case 1
                                        // 2. Remedy: LR rotation.
                                        //          = right-rotation(node_v) +
                                        //            left-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rr1 - case1");
                                        node_w.right.color = BLACK;
                                        rbst_right_rotate(node_v);
                                        rbst_left_rotate(node_py);
                                        // 3. Deficiency eliminated.
                                        // 4. Done!
                                    }
                                    else {
                                        // 1. case 2
                                        // 2. Remedy: right-rotation(node_w) +
                                        //            right-rotation(node_v) +
                                        //            left-rotation(node_py)
                                        RbstUtil.rbst_log("case: Rr1 - case2");
                                        node_w.left.color = BLACK;
                                        rbst_right_rotate(node_w);
                                        rbst_right_rotate(node_v);
                                        rbst_left_rotate(node_py);
                                        // 3. Deficiency eliminated.
                                        // 4. Done!
                                    }
                                } // case Lr1
                                break;
                                case Lr2:
                                {
                                    // 1. case Rr2
                                    // 2. Remedy: right-rotation(node_w) +
                                    //            right-rotation(node_v) +
                                    //            left-rotation(node_py)
                                    RbstUtil.rbst_log("case:Rr2");
                                    node_w.left.color = BLACK;
                                    rbst_right_rotate(node_w);
                                    rbst_right_rotate(node_v);
                                    rbst_left_rotate(node_py);
                                    // 3. Deficiency eliminated.
                                    // 4. Done!
                                } // case Lr2
                                break;
                                default:
                                {
                                    RbstUtil.rbst_log("Unhandled case:" 
                                    + " Rr" + n);
                                    RbstUtil.rbst_log("rbst_delete_fix_up:<\n");
                                    return;
                                }
                            }
                            node_py = null;
                        } // case Rrn
                    } // case Lcn
                } //if(node_py != null)
            } //if(node_y.color == RED)
        } //while(node_py != null)

        // 1. if node_py is null then node_y is a black 
        //    root as there is no node_py.
        // 2. Entire tree is deficient. Done!
        if(root != null) {
            root.color = BLACK;
        }
    }

    /* decrease the count value if node with 'key_id' found.
     * decrease the value by 'm' */
    public boolean rbst_decrease(int key_id, int m) throws Exception {
        RbstNode node = null;
        boolean result = true;

        // 1. decrease the count value if node with 'key_id' found
        // 2. if count becomes 0 then delete the node
        // 3. if node not found then return false

        if (root != null) {
            if((node = rbst_find(key_id)) != null) {
                if(node.key_id == key_id) {
                    // decrease count value 
                    if(node.count > 0) {
                        node.count -= m;
                        if (node.count <= 0) {
                            RbstUtil.rbst_log_final(0);
                        }
                        else {
                            RbstUtil.rbst_log_final(node.count);
                        }
                    }

                    // delete node if count is 0
                    if (node.count <= 0) {
                        rbst_delete(key_id);
                    }
                }
                else
                {
                    RbstUtil.rbst_log("node not found");
                    RbstUtil.rbst_log_final("0");
                    result = false;
                }
            }
            else {
                RbstUtil.rbst_log("node not found");
                RbstUtil.rbst_log_final("0");
                result = false;
            }
        }
        else {
            RbstUtil.rbst_log("Invalid root");
            RbstUtil.rbst_log_final("0");
            result = false;
        }

        return result;
    }

    /* if the  node is not found then it returns
     * the leaf node which should be the parent 
     * of the node containing the input 'key_id'.
     * if root is null then it will return null.*/
    private RbstNode rbst_find(int key_id) {
        RbstNode node   = root;
        RbstNode parent = null;

        if(root != null) {
            while(node != null) {
                if(node.key_id == key_id) {
                    break;
                }
                else if(key_id < node.key_id){
                    parent = node;
                    node   = node.left;
                }
                else {
                    parent = node;
                    node   = node.right;
                }
            }
        }

        return (node != null) ? node : parent;
    }

    /* predecessor is the largest node in the
     * left sub-tree. find the predecessor */
    private RbstNode rbst_find_predecessor(RbstNode node_z) {
        RbstNode node = node_z.left;
        RbstNode prev = node_z;

        while(node != null) {
            prev = node;
            node = node.right;
        }

        if(prev == node_z) {
            prev = null;
        }

        return prev;
    }

    /* find the node with the greatest key
     * less than the 'key_id'.return null
     * if such a node is not found */
    private RbstNode rbst_find_predecessor(int key_id) {
        RbstNode node = root;
        RbstNode prev = null;

        while(node != null) {
            if(key_id > node.key_id) {
                // this is where we took the last right turn
                // mark it as prev
                prev = node;
                node = node.right;
            }
            else {
                node = node.left;
            }
        }

        return prev;
    }

   /* successor is the largest node in the
    * left sub-tree. find the successor */
    public RbstNode rbst_find_successor(RbstNode node_z) {
        RbstNode node = node_z.right;
        RbstNode succ = node_z;

        while(node != null) {
            succ = node;
            node = node.left;
        }

        if(succ == node_z) {
            succ = null;
        }

        return succ;
    }

    /* find the node with the lowest key 
     * greater than the 'key_id'. return
     * null if such a node is not found */
    private RbstNode rbst_find_successor(int key_id) {
        RbstNode node = root;
        RbstNode succ = null;

        while(node != null) {
            if(key_id < node.key_id) {
                // this is where we took the 
                // last left turn - mark it as succ
                succ = node;
                node = node.left;
            }
            else {
                node = node.right;
            }
        }

        return succ;
    }

    /* perform left-rotation of subtree rooted at 'node_x' */
    private boolean rbst_left_rotate(RbstNode node_x) {
        RbstNode node_y = null;
        boolean result = true;

        if(node_x != null) {
            node_y = node_x.right;
            node_x.right = node_y.left;
            if(node_y.left != null) {
                node_y.left.parent = node_x;
            }
            node_y.parent = node_x.parent;
            if (node_x.parent == null) {
                root = node_y;
            }
            else if (node_x == node_x.parent.left) {
                node_x.parent.left = node_y;
            }
            else {
                node_x.parent.right = node_y;
            }
            node_y.left = node_x;
            node_x.parent = node_y;
        }
        else {
            result = false;
        }

        return result;
    }

    /* perform right-rotation of subtree rooted at 'node_x' */
    private boolean rbst_right_rotate(RbstNode node_x) {
        RbstNode node_y = null;
        boolean result = true;

        if(node_x != null) {
            node_y  = node_x.left;
            node_x.left = node_y.right;
            if(node_y.right != null) {
                node_y.right.parent = node_x;
            }
            node_y.parent = node_x.parent;
            if (node_x.parent == null) {
                root = node_y;
            }
            else if (node_x == node_x.parent.right) {
                node_x.parent.right = node_y;
            }
            else {
                node_x.parent.left = node_y;
            }
            node_y.right  = node_x;
            node_x.parent = node_y;
        }
        else {
            result = false;
        }

        return result;
    }

    /* delete node_z from rbst. not exposed to user.
     * note that node_z is assumed to have only one
     * child - not both */
    private void rbst_delete_node(RbstNode node_z) {
        RbstNode p = node_z.parent;

        if(p == null) {
            // root node
            if(node_z.left != null) {
                root = node_z.left;
                node_z.left.parent = null;
            }
            else {
                root = node_z.right;
                if(node_z.right != null) {
                    node_z.right.parent = null;
                }
            }
        }
        else if (p.right == node_z){
            if(node_z.right != null) {
                p.right = node_z.right;
                node_z.right.parent = p;
            }
            else {
                p.right = node_z.left;
                if(node_z.left != null) {
                    node_z.left.parent = p;
                }
            }
        }
        else {
            if(node_z.right != null) {
                p.left = node_z.right;
            }
            else {
                p.left = node_z.left;
            }
        }

        rbst_inorder(root);
    }

    /* inorder traversal of the tree to see the
     * status of the tree */
    public void rbst_inorder(RbstNode node) {
        if(node != null) {
            rbst_inorder(node.left);
            RbstUtil.rbst_log_no_nl("id: " + node.key_id + 
                "  count: " + node.count + "  color: ");
            if(node.color == BLACK) { 
                RbstUtil.rbst_log_no_nl("BLACK");
            }
            else {
                RbstUtil.rbst_log_no_nl("RED");
            }
            if(node.left != null) {
                RbstUtil.rbst_log_no_nl(" left: " + 
                        node.left.key_id + " " + node.left.color);
            }
            else {
                RbstUtil.rbst_log_no_nl(" left: null");
            }

            if(node.right != null) {
                RbstUtil.rbst_log(" right: " + 
                        node.right.key_id + " " + node.right.color);
            }
            else {
                RbstUtil.rbst_log(" right: null");
            }
            rbst_inorder(node.right);
        }
    }

    /* inorder traversal of the tree to count
     * in range elements. it doesn traverse the
     * whole of 'node's left and right subtree.
     * it visits the nodes in the range
     * node1.key_id <= node.key_id <= node2.key_id.
     * it's running time is O(s) where s is #nodes
     * satisfying the above condition */
    private int rbst_inorder_count_inrange(RbstNode node, 
                                RbstNode node1, RbstNode node2) {
        int inRangeC = 0;
        if((node != null) && (node != node1) && (node != node2)) {
            inRangeC = rbst_inorder_count_inrange(node.left, node1, node2);
            if(node1.key_id <= node.key_id &&
                        node.key_id <= node2.key_id) {
                inRangeC += node.count;
            }
            inRangeC += rbst_inorder_count_inrange(node.right, node1, node2);
        }
        else if(node == node1) {
            inRangeC = rbst_inorder_count_inrange(node.right, node1, node2);
            if(node1.key_id <= node.key_id &&
                        node.key_id <= node2.key_id) {
                inRangeC += node.count;
            }
        }
        else if(node == node2) {
            inRangeC = rbst_inorder_count_inrange(node.left, node1, node2);
            if(node1.key_id <= node.key_id &&
                        node.key_id <= node2.key_id) {
                inRangeC += node.count;
            }
        }
        return inRangeC;
    }

    /* Swap node_x and node_y in rbst */
    private void rbst_swap_nodes(RbstNode node_x, RbstNode node_y) throws Exception {
        // clone node_y
        RbstNode temp = (RbstNode) node_y.clone();

        // node_y will take node_x's place
        /* copy node_x's children and parent as node_y's
         * parent and children */ 

        /* adjust node_x's parent's left or right pointer
         * depending on whether node_x is  currently its
         * parent's right or left pointer */
        if (node_x.parent != null) {
            if(node_x == node_x.parent.left) {
                node_x.parent.left = node_y;
            }
            else {
                node_x.parent.right = node_y;
            }
        }
        else {
            /* node_x is root. so after the swap 
             * node_y is the new root */
            root = node_y;
        }

        /* adjust node_y's new left, right and parent pointer */
        node_y.parent = node_x.parent;

        if(node_x.left == node_y) {
            node_y.left = node_x;
        }
        else {
            node_y.left   = node_x.left;
            if(node_x.left != null) {
                node_x.left.parent = node_y;
            }
        }

        if(node_x.right == node_y) {
            node_y.right = node_x;
        }
        else {
            node_y.right = node_x.right;
            if(node_x.right != null) {
                node_x.right.parent = node_y;
            }
        }

        /* node_y will retain node_x's color */
        node_y.color = node_x.color;


        /* Now, we need to re-adjust the pointers of
         * node_x such that it takes node_y's place */

        /* adjust node_y's parent's left or right pointer
         * depending on whether node_y is  currently its
         * parent's right or left pointer */
        if (temp.parent != null) {
            if(node_y == temp.parent.left) {
                temp.parent.left = node_x;
            }
            else {
                temp.parent.right = node_x;
            }
        }
        else {
            /* node_y is root. so after the swap 
             * node_x is the new root */
            root = node_x;
        }

        /* adjust node_x's new left, right and parent pointer */
        if(temp.parent == node_x) {
            node_x.parent = node_y;
        }
        else {
            node_x.parent = temp.parent;
        }

        node_x.left   = temp.left;
        if(temp.left != null) {
            temp.left.parent = node_x;
        }
        node_x.right  = temp.right;
        if(temp.right != null) {
            temp.right.parent = node_x;
        }

        /* node_x will retain node_y's color */
        node_x.color = temp.color;
    }

    /* print the count for ids between 'key_id1' and
     * 'key_id2'. Note that 'key_id1 < key_id2'. In
     * the worst case it takes '4logn + s' or O(logn + s)
     * time where n is the no of elements in the tree
     * and s is the no of elements in the range */
    public void rbst_print_in_range(int key_id1, int key_id2) {
        if(key_id1 <= key_id2) {
            // find node with key-id1
            // O(logn)
            RbstNode node1 = rbst_find(key_id1);
            // find node with key-id2
            // O(logn)
            RbstNode node2 = rbst_find(key_id2);

            if(!(node1 != null && node1.key_id == key_id1)) {
                // if node with key_id1 is nor found then
                // find its successor
                // O(logn)
                node1 = rbst_find_successor(key_id1);
            }

            if(!(node2 != null && node2.key_id == key_id2)) {
                // if node with key_id1 is nor found then
                // find its predecessor
                // O(logn)
                node2 = rbst_find_predecessor(key_id2);
            }

            // node1 is the start of the range in the tree
            // node2 is the end of the range in the tree

            if(node1 != null && node2 != null) {
                int inRangeCount = 0;
                RbstNode node = root;

                // O(s)
                while(node != null) {
                    if(node1.key_id <= node.key_id &&
                        node.key_id <= node2.key_id) {
                        /* if node is between node1 and
                         * and node2 then we need to 
                         * traverse both the subtrees of
                         * node */
                        inRangeCount = rbst_inorder_count_inrange(node, node1, 
                        node2);
                        node = null;
                    }
                    else if(node2.key_id < node.key_id) {
                        /* entire range is in the left
                         * subtree */
                        node = node.left;
                    }
                    else if(node1.key_id > node.key_id) {
                        /* entire range is in the right
                         * subtree */
                        node = node.right;
                    }
                }
                RbstUtil.rbst_log_final(inRangeCount);
            }
            else {
                RbstUtil.rbst_log_final(0);
            }
        }
        else {
             RbstUtil.rbst_log_final(0);
        }
    }

    /* print the 'key_id' and the 'count' of the node
     * with the greatest key less than the 'key_id'.
     * print "0 0" if there is no such id */
    public void rbst_print_previous(int key_id) {

        // find predecessor
        RbstNode pred = rbst_find_predecessor(key_id);
        if(pred != null) {
            RbstUtil.rbst_log_final(pred.key_id + " " + pred.count);
        }
        else {
            // previous not found
            RbstUtil.rbst_log_final("0 0");
        }
    }

    /* print the 'key_id' and the 'count' of the node
     * with the lowest key greater than the 'key_id'.
     * print "0 0" if there is no such id */
    public void rbst_print_next(int key_id) {
        // find predecessor
        RbstNode succ = rbst_find_successor(key_id);
        if(succ != null) {
            RbstUtil.rbst_log_final(succ.key_id + " " + succ.count);
        }
        else {
            // next not found
            RbstUtil.rbst_log_final("0 0");
        }
    }

    /* print the 'count' of the node with id 'key_id'
    .* print 0 if not found */
    public void rbst_print_count(int key_id) {
        // find the node with 'key_id'
        RbstNode node = rbst_find(key_id);

        if(node != null) {
            if(node.key_id == key_id) {
                RbstUtil.rbst_log_final(node.count);
            }
            else {
                // node not found
                RbstUtil.rbst_log_final("0");
            }
        }
        else {
            // rbst invalid. root is null 
            RbstUtil.rbst_log_final("0");
        }
    }

    /* print the content of the node named 'nodeName' */
    public void rbst_print_node(RbstNode node, String nodeName) {
        RbstUtil.rbst_log(nodeName + ".key_id: " + node.key_id);
        RbstUtil.rbst_log(nodeName + ".count: " + node.count);
        RbstUtil.rbst_log(nodeName + ".color: " + node.color);
        if(node.parent != null) {
            RbstUtil.rbst_log(nodeName + ".parent: " + node.parent.key_id);
        }
        else {
            RbstUtil.rbst_log(nodeName + ".parent: null");
        }

        if(node.left != null) {
            RbstUtil.rbst_log(nodeName + ".left: " + node.left.key_id);
        }
        else {
            RbstUtil.rbst_log(nodeName + ".left: null");
        }

        if(node.right != null) {
            RbstUtil.rbst_log(nodeName + ".right: " + node.right.key_id+ "\n");
        }
        else {
            RbstUtil.rbst_log(nodeName + ".right: null\n");
        }
    }

    public static void main(String[] args) throws Exception {

        try {
            if(args.length < 1) {
                RbstUtil.rbst_log("Not enough input");
                System.exit(0);
            }
            RbstUtil.rbst_log("No of ips: " + args.length);
            //Get file from ip
            BufferedReader bufRd = new BufferedReader(new InputStreamReader(
                                                                    System.in));
            String fileName = new String(args[0]);
            String currDir  = new String(System.getProperty("user.dir"));

            // test to see if file exists in default path
            File ipFile = new File(fileName);
            if(ipFile.exists()) {
                Scanner scanner = new Scanner(ipFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(new 
                FileInputStream(ipFile)));
                String nextLine = null;
                String[] tokens = null;
                String delimiters = "\\s+|,\\s*|\\.\\s*";

                //read the no of elements from the file
                if(scanner.hasNextInt()) {
                    noOfElements = scanner.nextInt();
                    int i = 0, k = 0;
                    /* dummy read to go across the first line
                     * in the file. we are avoiding the scanner
                     * in the while loop below to minimize read
                     * time from the file */
                    br.readLine();

                    // create an array of rbst nodes from the ip array
                    ArrayList <RbstNode> rbstNodes = new ArrayList<RbstNode>();
                    while(k < noOfElements) {
                        //create node with next 'key_id' and 'count'
                        RbstNode node = new bbst.RbstNode();
                        nextLine      = br.readLine();
                        tokens = nextLine.split(delimiters);
                        //node.key_id = scanner.nextInt();
                        //node.count  = scanner.nextInt();
                        node.key_id = Integer.parseInt(tokens[0]);
                        node.count  = Integer.parseInt(tokens[1]);
                        //initialize to BLACK
                        node.color  = BLACK;
                        //initialize left and right child pointer
                        node.parent = node.left = node.right = null;
                        //put node in <RbstNode>
                        rbstNodes.add(node); k++;
                    }

                    //create RB tree in O(n) time
                    RbstNode[] rbstnodes = rbstNodes.toArray(new RbstNode[rbstNodes.size()]);
                    bbst rbst = new bbst(rbstnodes);


                    //command loop
                    while(true) {
                        // read the next cmd
                        String cmd = bufRd.readLine();

                        if(cmd != null) {
                            // split the cmd by space
                            tokens = cmd.split(delimiters);
                        }
                        else {
                            tokens = new String[1];
                            tokens[0] = "quit";
                        }

                        switch(tokens[0].toLowerCase()) {
                            case "insert":
                            {
                                int key_id_i = 0, count_i = 0;
                                String key_id = new String(tokens[1]);
                                String count  = new String(tokens[2]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                //convert the string value of 'count' to integer
                                i = 0;
                                while(i < count.length()) {
                                    count_i = 10*count_i + 
                                    count.toCharArray()[i++] - 48;
                                }

                                RbstNode node = new bbst.RbstNode();
                                node.key_id = key_id_i;
                                node.count  = count_i;
                                node.color  = BLACK;
                                node.parent = node.left = node.right = null;

                                rbst.rbst_insert(node);
                            }
                            break;
                            case "delete":
                            {
                                int key_id_i = 0;
                                String key_id = new String(tokens[1]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_delete(key_id_i);
                            }
                            break;
                            case "reduce":
                            {
                                int key_id_i = 0, m_i = 0;
                                String key_id = new String(tokens[1]);
                                String m  = new String(tokens[2]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                //convert the string value of 'count' to integer
                                i = 0;
                                while(i < m.length()) {
                                    m_i = 10*m_i + m.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_decrease(key_id_i, m_i);
                            }
                            break;
                            case "increase":
                            {
                                int key_id_i = 0, m_i = 0;
                                String key_id = new String(tokens[1]);
                                String m  = new String(tokens[2]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                //convert the string value of 'count' to integer
                                i = 0;
                                while(i < m.length()) {
                                    m_i = 10*m_i + m.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_increase(key_id_i, m_i);
                            }
                            break;
                            case "count":
                            {
                                int key_id_i = 0;
                                String key_id = new String(tokens[1]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_print_count(key_id_i);
                            }
                            break;
                            case "previous":
                            {
                                int key_id_i = 0;
                                String key_id = new String(tokens[1]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_print_previous(key_id_i);
                            }
                            break;
                            case "next":
                            {
                                int key_id_i = 0;
                                String key_id = new String(tokens[1]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id.length()) {
                                    key_id_i = 10*key_id_i + 
                                    key_id.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_print_next(key_id_i);
                            }
                            break;
                            case "inrange":
                            {
                                int key_id_i1 = 0, key_id_i2 = 0;
                                String key_id1 = new String(tokens[1]);
                                String key_id2 = new String(tokens[2]);

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id1.length()) {
                                    key_id_i1 = 10*key_id_i1 + 
                                    key_id1.toCharArray()[i++] - 48;
                                }

                                //convert the string value of 'key_id' to integer
                                i = 0;
                                while(i < key_id2.length()) {
                                    key_id_i2 = 10*key_id_i2 + 
                                    key_id2.toCharArray()[i++] - 48;
                                }

                                rbst.rbst_print_in_range(key_id_i1, key_id_i2);
                            }
                            break;
                            case "quit":
                            {
                                RbstUtil.rbst_log("Bye...");
                                System.exit(0);
                            }
                            break;
                            default:
                            {
                                RbstUtil.rbst_log("cmd not found..." + 
                                                cmd.toCharArray()[0] + 48);
                            }
                        }//end switch
                    }//end while
                }
                else {
                    RbstUtil.rbst_log("Improper File format");
                }
                br.close();
                scanner.close();
            }
            else {
                RbstUtil.rbst_log_final("File does not exist in pwd");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
        }
    }
}
