package com.example.redisexample.dao.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")

//TODO: Show this   Bu annotasiyanın məqsədi manual configurasiyada biz redisTemplate götürdüyü
// obyekt üçün hasValueSerializer artırmasaq Entity özündə qeyd etməliyik, Implements serializable da buna nümunədir.
//@RedisHash("User")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDateTime birthDate;
    private Integer age;
    private String description;
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
