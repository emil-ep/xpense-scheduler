package com.xperia.xpense_scheduler.services;

import com.xperia.xpense_scheduler.models.entity.mf.MutualFundScheme;

import java.util.List;
import java.util.Optional;

public interface MutualFundSchemeService {

    Optional<List<MutualFundScheme>> findAllSchemes();
}
