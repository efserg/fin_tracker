package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentableTransaction extends Transaction implements Commentable {

    List<String> comments;

    public CommentableTransaction(int accountId, int transactionId,
                                  LocalDateTime date, String category,
                                  BigDecimal amount, List<String> comments) {
        super(accountId, transactionId, date, category, amount);
        this.comments = comments;
    }

    @Override
    public List<String> getComments() {
        return List.copyOf(comments);
    }

}
