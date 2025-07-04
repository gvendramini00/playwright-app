package org.gig.myplayrightapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.repository.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public boolean existsByAlias(String alias) {
        return alias != null && playerRepository.findByAlias(alias).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return email != null && playerRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsByPhone(String phone) {
        return phone != null && playerRepository.findByPhone(phone).isPresent();
    }

    @Override
    public boolean existsByNationalId(String nationalId) {
        return nationalId != null && playerRepository.findByNationalId(nationalId).isPresent();
    }

    @Override
    public boolean existsAnyMatching(String alias, String phone, String nationalId, String email) {
        return playerRepository.existsByAny(alias, phone, nationalId, email);
    }
}
