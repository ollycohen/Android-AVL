/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right, parent;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);
    private int balance;

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = true;
        this.left = null;
        this.right = null;
        this.parent = null;
    }


    public int getHeight() {return height;}

    public void setBalance() {
        int r;
        int l;
        if(right == null) {
            r = -1;
        }
        else {
            r = right.getHeight();
        }
        if(left == null){
            l = -1;
        }
        else {
            l = left.getHeight();
        }
        balance = r - l;


        if (balance == -2) {
            if (left != null && left.balance == 1) {
                left = left.leftRotate();
            }
            if (parent == null) {
                this.rightRotate();
            }
            if(this == parent.right) {
                parent.right = rightRotate();
            }
            else  {
                parent.left = rightRotate();
            }
        }


        else if (balance == 2) {
            if (right != null && right.balance == -1) {
                right = right.rightRotate();
            }
            if (parent == null) {
                this.leftRotate();
            }
            else if (this == parent.left) {
                parent.left = leftRotate();
            }
            else {
                parent.right = leftRotate();
            }
        }


    }

    public TreeNode rightRotate() {
        TreeNode newroot = left;
        TreeNode newleft = left.right;

        newroot.right = this;
        left = newleft;

        newroot.parent = this.parent;
        this.parent = newroot;


        updateHeight();
        newroot.updateHeight();


        return newroot;
    }

    public void updateHeight() {
        if(this == null) {
            height = -1;
        }
        else if(right == null && left == null) {
            height = 0;
        }
        else if (right == null) {
            height = left.getHeight() + 1;
        }
        else if (left == null) {
            height = right.getHeight() +1;
        }
        else {
            height = Math.max(right.getHeight(), left.getHeight()) + 1;
        }
        setBalance();
    }


    public TreeNode leftRotate() {
        TreeNode newRoot = right;
        TreeNode newRight = right.left;

        newRoot.left = this;
        right = newRight;

        newRoot.parent = this.parent;
        this.parent = newRoot;

        updateHeight();
        newRoot.updateHeight();

        return newRoot;
    }



    public void insert(int valueToInsert) {
        if (valueToInsert < value) {
            if (left == null) {
                left = new TreeNode(valueToInsert);
                left.height = 0;
                left.parent = this;
                updateHeight();

            } else {
                left.insert(valueToInsert);
                updateHeight();
            }

        } else {
            if (right == null) {
                right = new TreeNode(valueToInsert);
                right.height = 0;
                right.parent = this;
                updateHeight();

            } else {
                right.insert(valueToInsert);
                updateHeight();

            }


        }
    }







    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
