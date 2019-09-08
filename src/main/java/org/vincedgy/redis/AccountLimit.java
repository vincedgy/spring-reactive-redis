package org.vincedgy.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountLimit {
    private String account;
    private String date;
    private String limit;
    private String key;

    AccountLimit(String key) {
        this.key = key;
    }
}
