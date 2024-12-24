package org.koreait.global.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteConfig {
    private String siteTitle;
    private String description;
    private String keywords;
    private int cssVersion;
    private int jsVersion;
    private boolean useEmailAuth;
}
