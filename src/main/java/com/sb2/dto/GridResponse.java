package com.sb2.dto;

import com.sb2.constant.GridStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
public class GridResponse {

    private Long studentId;

    private Long tutorId;

    private GridStatus gridStatus;

    private Map<String, Integer> scores;
}
