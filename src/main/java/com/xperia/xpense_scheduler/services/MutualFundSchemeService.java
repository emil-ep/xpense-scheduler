package com.xperia.xpense_scheduler.services;

import org.xperia.entities.mf.MutualFundScheme;

import java.util.List;
import java.util.Optional;

public interface MutualFundSchemeService {

    Optional<List<MutualFundScheme>> findAllSchemes();
}
