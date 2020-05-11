package com.muttsApp.POJOs;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;
    @NotEmpty(message = "message may not be empty")
    private String content;
    //    @Column()
    @Column(columnDefinition = "timestamp default current_timestamp", updatable= false, insertable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
