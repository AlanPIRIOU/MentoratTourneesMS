package fr.su.mentorattourneesms.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Schema(name = "Tournee", description = "Tournee")
public class Tournee {

    public static final String COL_CODE = "MATRICULE";
    public static final String COL_LIBELLE = "NOMSALARIE";

    @Id
    String code;

    String libelle;


}
