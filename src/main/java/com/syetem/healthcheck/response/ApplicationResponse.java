package com.syetem.healthcheck.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    Long id;
    String applicationName;
    String upTime;
    boolean status;
}
