package com.trademind.billing.repository;

import com.trademind.billing.entity.QRCodeMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QRCodeMetadataRepository extends JpaRepository<QRCodeMetadata, Long> {}