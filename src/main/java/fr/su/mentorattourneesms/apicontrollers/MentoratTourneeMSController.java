package fr.su.mentorattourneesms.apicontrollers;

import fr.su.back.common.exception.BusinessException;
import fr.su.mentorattourneesms.model.Tournee;
import fr.su.mentorattourneesms.services.MentoratTourneeMSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/entrepots/{entrepot}")
@Tag(name = "Mentorattourneems", description = "")
public class MentoratTourneeMSController {

    @Autowired
    private MentoratTourneeMSService mentoratTourneeMSService;

    @GetMapping("/tournees")
    @ResponseStatus(HttpStatus.OK)
    @Operation(parameters = {
            @Parameter(name = "entrepot", in = ParameterIn.PATH, schema = @Schema(implementation = String.class))}, tags = "Entrepots", summary = "Liste des imprimantes", description = "Récupération de la liste des imprimantes [ nomImprimante1,...] ", security = {
    })
    public ResponseEntity<List<Tournee>> listeTournees() throws BusinessException {
        return ResponseEntity.ok(mentoratTourneeMSService.findTournees());
    }

}
