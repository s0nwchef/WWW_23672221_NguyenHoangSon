package fit.se.demobookstore.beans;

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

    public void addBook(Book b) {
        for (CartItemBean item : items) {
            if (item.getBook().getId() == b.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItemBean(b, 1));
    }

    public void removeBook(int bookId) {
        items.removeIf(item -> item.getBook().getId() == bookId);
    }

    public void updateQuantity(int bookId, int quantity) {
        for (CartItemBean item : items) {
            if (item.getBook().getId() == bookId) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    removeBook(bookId);
                }
                return;
            }
        }
    }

    // tính tổng số lượng sách trong giỏ (để hiển thị "Shopping cart (n)")
    public int getItemCount() {
        int count = 0;
        for (CartItemBean item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    // tính tổng tiền
    public double getTotal() {
        double total = 0;
        for (CartItemBean item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // xóa hết giỏ hàng
    public void clear() {
        items.clear();
    }
}
