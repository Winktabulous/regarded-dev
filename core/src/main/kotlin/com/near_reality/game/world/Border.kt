package com.near_reality.game.world

data class Border(val type: BorderType, val start: Int, val end: Int, val otherCross: Int) {

    companion object {
        fun ofX(start: Int, end: Int, y: Int) : Border {
            return Border(BorderType.X, start, end, y)
        }

        fun ofY(start: Int, end: Int, x: Int) : Border {
            return Border(BorderType.Y, start, end, x)
        }

        fun fromBoundary(boundary: Boundary, borderDir: BoundaryBorder) : Border {
            return when(borderDir) {
                BoundaryBorder.NORTH -> {
                    Border(BorderType.X, boundary.minX, boundary.highX, boundary.highY)
                }

                BoundaryBorder.SOUTH -> {
                    Border(BorderType.X, boundary.minX, boundary.highX, boundary.minY)
                }

                BoundaryBorder.EAST -> {
                    Border(BorderType.Y, boundary.minY, boundary.highY, boundary.highX)
                }

                BoundaryBorder.WEST -> {
                    Border(BorderType.Y, boundary.minY, boundary.highY, boundary.minX)
                }
            }
        }
    }

    enum class BoundaryBorder {
        NORTH, SOUTH, EAST, WEST
    }

    /* X is horizontal, Y is vertical */
    enum class BorderType {
        X, Y
    }

}