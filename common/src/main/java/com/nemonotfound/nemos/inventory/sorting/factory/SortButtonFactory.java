package com.nemonotfound.nemos.inventory.sorting.factory;

public abstract class SortButtonFactory implements ButtonCreator {

    protected int getLeftPosWithOffset(int leftPos, int imageWidth, int offset) {
        return leftPos + imageWidth - offset;
    }
}
