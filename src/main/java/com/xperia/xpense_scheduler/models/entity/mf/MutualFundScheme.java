package com.xperia.xpense_scheduler.models.entity.mf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MutualFundScheme {

    private String schemeCode;

    private String schemeName;

    private String isinGrowth;

    private String isinDivReinvestment;
}
