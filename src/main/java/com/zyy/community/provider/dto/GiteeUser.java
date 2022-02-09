package com.zyy.community.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiteeUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

    @Override
    public String toString() {
        return "GiteeUser{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", bio='" + bio + '\'' +
                '}';
    }
}
