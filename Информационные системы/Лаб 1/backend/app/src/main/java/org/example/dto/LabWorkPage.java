package org.example.dto;

import java.util.List;
import org.example.entity.LabWork;

public record LabWorkPage(List<LabWork> items, long total, int page, int size) {
}
