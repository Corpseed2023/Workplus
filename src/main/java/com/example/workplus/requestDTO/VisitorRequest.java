package com.example.workplus.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRequest {

    private String name;

    private String contactNumber;

    private String visitorMailId;

    private String companyName;

    private String referenceThrough;

    private String purpose;

}
