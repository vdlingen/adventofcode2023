package day24

import com.microsoft.z3.Context
import kotlin.math.abs

val input = util.readInput("day24.txt")
    .split("\n").map {
        val (position, velocity) = it.split(" @ ")

        Entry(
            position.split(", ").map { it.trim().toDouble() }.let { Vec3(it[0], it[1], it[2]) },
            velocity.split(", ").map { it.trim().toDouble() }.let { Vec3(it[0], it[1], it[2]) },
        )
    }

data class Vec3(val x: Double, val y: Double, val z: Double)
data class Vec2(val x: Double, val y: Double)

data class Entry(val position: Vec3, val velocity: Vec3)

fun Entry.intersection(other: Entry): Vec2? {
    val m1 = velocity.y / velocity.x
    val m2 = other.velocity.y / other.velocity.x

    if (abs(m2 - m1) < 0.000000000001) return null

    val x = (m1 * position.x - m2 * other.position.x + other.position.y - position.y) / (m1 - m2)
    val y = (m1 * m2 * (other.position.x - position.x) + m2 * position.y - m1 * other.position.y) / (m2 - m1)

    return Vec2(x, y)
}

fun part1(): Int {
    val range = 200000000000000.0..400000000000000.0

    var intersections = 0

    input.forEachIndexed { index, line1 ->
        input.drop(index + 1).forEach { line2 ->
            line1.intersection(line2)?.let { (x, y) ->
                if ((line1.velocity.x < 0 && x > line1.position.x) || (line1.velocity.x > 0 && x < line1.position.x))
                    return@forEach

                if ((line2.velocity.x < 0 && x > line2.position.x) || (line2.velocity.x > 0 && x < line2.position.x))
                    return@forEach

                if (x in range && y in range) {
                    intersections++
                }
            }
        }
    }

    return intersections
}

/**
 * Uses Z3 solver library. Requires the libz3java.so and libz3.so binaries to
 * be installed in /usr/lib.
 */
fun part2() = with(Context()) {
    val x = mkRealConst("x")
    val y = mkRealConst("y")
    val z = mkRealConst("z")
    val dx = mkRealConst("dx")
    val dy = mkRealConst("dy")
    val dz = mkRealConst("dz")

    val result = mkRealConst("result")

    val s = mkSolver()

    s.add(mkEq(result, mkAdd(x, y, z)))

    (0..2).forEach { index ->
        val entry = input[index]

        val t = mkRealConst("t$index")

        s.add(mkGe(t, mkInt(0)))
        s.add(
            mkEq(
                mkAdd(x, mkMul(t, dx)),
                mkAdd(mkInt(entry.position.x.toLong()), mkMul(t, mkInt(entry.velocity.x.toLong())))
            )
        )
        s.add(
            mkEq(
                mkAdd(y, mkMul(t, dy)),
                mkAdd(mkInt(entry.position.y.toLong()), mkMul(t, mkInt(entry.velocity.y.toLong())))
            )
        )
        s.add(
            mkEq(
                mkAdd(z, mkMul(t, dz)),
                mkAdd(mkInt(entry.position.z.toLong()), mkMul(t, mkInt(entry.velocity.z.toLong())))
            )
        )
    }

    s.check()
    s.model.getConstInterp(result).toString()
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}