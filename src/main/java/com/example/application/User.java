package com.example.application;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private String role;

    @Override
    public final boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public final int hashCode(){
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getImplementation().hashCode() : getClass().hashCode();
    }

}
