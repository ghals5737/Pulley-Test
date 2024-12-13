package org.example.pulleyapi.piece.service

import org.example.pulleyapi.piece.respository.PieceRepository
import org.springframework.stereotype.Service

@Service
class PieceServiceImpl (
    private val pieceRepository: PieceRepository
): PieceService {

}