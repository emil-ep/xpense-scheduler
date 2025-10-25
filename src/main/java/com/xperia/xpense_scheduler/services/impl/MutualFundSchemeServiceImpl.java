package com.xperia.xpense_scheduler.services.impl;

import com.xperia.xpense_scheduler.services.MutualFundSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xperia.entities.mf.MutualFundScheme;
import org.xperia.repository.mf.MutualFundSchemeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MutualFundSchemeServiceImpl implements MutualFundSchemeService {

    @Autowired
    private MutualFundSchemeRepository mutualFundSchemeRepository;

    @Override
    public Optional<List<MutualFundScheme>> findAllSchemes() {
        return Optional.of(mutualFundSchemeRepository.findAll());
    }
}
