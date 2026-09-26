package fit.se.democart.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {
    private List<CartItemBean> items;

    public CartBean() {
        items = new ArrayList<>();
    }

    public List<CartItemBean> getItems() {
        return items;
    }

    public void addProduct(Product p) {
        for (CartItemBean item : items) {
            if (item.getProduct().getId() == p.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItemBean(p, 1));
    }
    public void removeProduct(int productId) {
        items.removeIf(item -> item.getProduct().getId() == productId);
    }

    public void updateQuantity(int productId, int quantity) {
        for (CartItemBean item : items) {
            if (item.getProduct().getId() == productId) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    removeProduct(productId);
                }
                return;
            }
        }
    }

    public double getTotal() {
        double total = 0;
        for (CartItemBean item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}
