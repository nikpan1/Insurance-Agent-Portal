package com.policytracker.referencedataimport.api.dto;

import java.util.List;

public record ReferenceDataNode(
        String id,
        String parentId,
        String name,
        List<ReferenceDataNode> children
) {
}
