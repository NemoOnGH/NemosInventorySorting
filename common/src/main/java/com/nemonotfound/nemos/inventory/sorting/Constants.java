package com.nemonotfound.nemos.inventory.sorting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "nemos_inventory_sorting";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
	public static String SORT_ALPHABETICALLY_CONTAINER = "sort_alphabetically_container";
	public static String SORT_ALPHABETICALLY_DESCENDING_CONTAINER = "sort_alphabetically_descending_container";
	public static String MOVE_SAME_CONTAINER = "move_same_container";
	public static String MOVE_ALL_CONTAINER = "move_all_container";
	public static String DROP_ALL_CONTAINER = "drop_all_container";
	public static String SORT_ALPHABETICALLY_CONTAINER_INVENTORY = "sort_alphabetically_container_inventory";
	public static String SORT_ALPHABETICALLY_DESCENDING_CONTAINER_INVENTORY = "sort_alphabetically_descending_container_inventory";
	public static String MOVE_SAME_CONTAINER_INVENTORY = "move_same_container_inventory";
	public static String MOVE_ALL_CONTAINER_INVENTORY = "move_all_container_inventory";
	public static String DROP_ALL_CONTAINER_INVENTORY = "drop_all_container_inventory";
	public static String SORT_ALPHABETICALLY_INVENTORY = "sort_alphabetically_inventory";
	public static String SORT_ALPHABETICALLY_DESCENDING_INVENTORY = "sort_alphabetically_descending_inventory";
	public static String DROP_ALL_INVENTORY = "drop_all_inventory";
	public static String ITEM_FILTER = "item_filter";
	public static int X_OFFSET_DROP_ALL_CONTAINER = 82;
	public static int X_OFFSET_MOVE_ALL_CONTAINER = 100;
	public static int X_OFFSET_MOVE_SAME_CONTAINER = 118;
	public static int X_OFFSET_SORT_ALPHABETICALLY_CONTAINER = 136;
	public static int X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_CONTAINER = 154;
	public static int Y_OFFSET_CONTAINER = 5;
	public static int X_OFFSET_DROP_ALL_INVENTORY = 127;
	public static int X_OFFSET_SORT_ALPHABETICALLY_INVENTORY = 142;
	public static int X_OFFSET_SORT_ALPHABETICALLY_DESCENDING_INVENTORY = 157;
	public static int Y_OFFSET_INVENTORY = 65;
	public static int X_OFFSET_ITEM_FILTER = 89;
	public static int Y_OFFSET_ITEM_FILTER = -16;
	public static int ITEM_FILTER_WIDTH = 84;
	public static int ITEM_FILTER_HEIGHT = 15;
	public static int BUTTON_SIZE = 11;

	public static final int MAX_MERGING_CYCLES = 1000;
	public static final int MAX_SORTING_CYCLES = 1000;
}