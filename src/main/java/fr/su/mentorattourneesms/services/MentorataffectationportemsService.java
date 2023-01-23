package fr.su.mentorattourneesms.services;

import fr.su.mentorattourneesms.repositories.MentorataffectationportemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MentorataffectationportemsService {

    @Autowired
    private MentorataffectationportemsRepository mentorataffectationportemsRepository;

}
