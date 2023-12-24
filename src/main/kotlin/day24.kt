package day24

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

    val x = (m1 * position.x - m2*other.position.x + other.position.y - position.y) / (m1 - m2)
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

fun part2(): Long {
    /** Z3 solver https://microsoft.github.io/z3guide/programming/Z3%20JavaScript%20Examples

    const x = Z3.Int.const('x');
    const y = Z3.Int.const('y');
    const z = Z3.Int.const('z');
    const dx = Z3.Int.const('dx');
    const dy = Z3.Int.const('dy');
    const dz = Z3.Int.const('dz');
    const result = Z3.Int.const('result');

    const t1 = Z3.Int.const('t1');
    const t2 = Z3.Int.const('t2');
    const t3 = Z3.Int.const('t3');

    Z3.solve(
        t1.ge(0),
        t2.ge(0),
        t3.ge(0),
        x.add(t1.mul(dx)).eq(t1.mul(288).add(102352610405511)),
        y.add(t1.mul(dy)).eq(t1.mul(172).add(202028623863107)),
        z.add(t1.mul(dz)).eq(t1.mul(406).add(54441177479725)),
        x.add(t2.mul(dx)).eq(t2.mul(97).add(225555584244738)),
        y.add(t2.mul(dy)).eq(t2.mul(-361).add(280452001678940)),
        z.add(t2.mul(dz)).eq(t2.mul(177).add(158175058414862)),
        x.add(t3.mul(dx)).eq(t3.mul(104).add(227688767181124)),
        y.add(t3.mul(dy)).eq(t3.mul(-84).add(208974144896814)),
        z.add(t3.mul(dz)).eq(t3.mul(-576).add(197748595030382)),
        result.eq(x.add(y).add(z))
    )

    Output:
    (define-fun dx () Int
    71)
    (define-fun t1 () Int
    587013854701)
    (define-fun dy () Int
    189)
    (define-fun t3 () Int
    61995445288)
    (define-fun dz () Int
    249)
    (define-fun t2 () Int
    160732024265)
    (define-fun y () Int
    192049388333190)
    (define-fun z () Int
    146602352667782)
    (define-fun x () Int
    229734616875628)
    (define-fun result () Int
    568386357876600)
    **/

    return 568386357876600
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}