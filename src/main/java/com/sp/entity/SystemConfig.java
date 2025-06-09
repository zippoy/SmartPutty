package com.sp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class SystemConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String key;
    private String value;

    public SystemConfig(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public SystemConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
