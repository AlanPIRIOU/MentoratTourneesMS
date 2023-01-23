package fr.su.mentorattourneesms.services;

import fr.su.mentorattourneesms.repositories.MentoratTourneeMSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MentoratTourneeMSService {

    @Autowired
    private MentoratTourneeMSRepository mentoratTourneeMSRepository;

}
