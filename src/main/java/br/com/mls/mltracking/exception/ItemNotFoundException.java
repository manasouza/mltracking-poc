package br.com.mls.mltracking.exception;

/**
 * Created by manasses on 9/14/16.
 */
public class ItemNotFoundException extends Throwable {

    private final String itemId;

    public ItemNotFoundException(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getMessage() {
        return "Item not found: " + itemId;
    }
}
