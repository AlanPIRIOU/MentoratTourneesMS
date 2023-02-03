package fr.su.mentorattourneesms.services;

import fr.su.back.common.exception.BusinessException;
import fr.su.mentorattourneesms.model.Tournee;
import fr.su.mentorattourneesms.repositories.MentoratTourneeMSRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MentoratTourneeMSService {

    @Autowired
    private MentoratTourneeMSRepository mentoratTourneeMSRepository;

    public List<Tournee> findTournees() throws BusinessException {
        return mentoratTourneeMSRepository.findTournees();
    }
}
