package model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.stereotype.Component;
import repository.types.PostgresEnumType;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"author\"")
@TypeDef(name = "pgsql_enum", typeClass = PostgresEnumType.class)
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    @Column(name = "name")
    private String name;
    @Type(type = "pgsql_enum")
    @Enumerated(EnumType.STRING)
    private Sex sex;
    public enum Sex {
        M, F
    }
}
