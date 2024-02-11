package com.craft.demo.models.exceptions;

import com.craft.demo.utils.CraftConstants;

public class SortConditionNotSupportedException extends Exception {
    public SortConditionNotSupportedException() {
        super(CraftConstants.SORT_CONDITION_NOT_SUPPORTED_EXCEPTION);
    }
}
