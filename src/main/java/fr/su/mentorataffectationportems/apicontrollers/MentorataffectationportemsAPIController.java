package fr.su.mentorataffectationportems.apicontrollers;

import fr.su.mentorataffectationportems.services.MentorataffectationportemsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/entrepots/{entrepot}")
@Tag(name = "Mentorataffectationportems", description = "")
public class MentorataffectationportemsAPIController {

    @Autowired
    private MentorataffectationportemsService mentorataffectationportemsService;

}