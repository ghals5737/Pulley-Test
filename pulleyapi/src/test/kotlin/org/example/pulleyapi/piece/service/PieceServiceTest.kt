package org.example.pulleyapi.piece.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PieceServiceTest @Autowired constructor(
    private val pieceService: PieceService
) {
}