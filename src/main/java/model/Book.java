package model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import repository.types.PostgresEnumType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "\"Book\"")
@TypeDef(name = "pgsql_enum", typeClass = PostgresEnumType.class)
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private String id;
    private String bookName;
    @Type(type = "pgsql_enum")
    private Topic topic;

    private int pageNumber;
    private Date releaseDate;
    @Type(type = "pgsql_enum")
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "author_id" , nullable = false)
    private Author author;
    public enum Topic {
        ROMANCE, COMEDY, OTHER
    }
    public enum Availability {
        Disponible, Borowed
    }
}
