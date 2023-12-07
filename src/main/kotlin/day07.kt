package day07

val input = util.readInput("day07.txt").split("\n").map {
    val (cards, bid) = it.split(" ")
    Hand(cards.toList().map { value -> Card.entries.find { it.char == value }!! }, bid.toInt())
}

enum class Card(val char: Char) {
    Joker('?'),
    Two('2'),
    Three('3'),
    Four('4'),
    Five('5'),
    Six('6'),
    Seven('7'),
    Eight('8'),
    Nine('9'),
    Ten('T'),
    Jack('J'),
    Queen('Q'),
    King('K'),
    Ace('A'),
}

enum class Type {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind,
}

fun List<Card>.type(): Type {
    val groups = groupBy { it }.mapValues { (_, value) -> value.size }

    val jokers = groups[Card.Joker] ?: 0
    val cards = groups.filter { it.key != Card.Joker }

    return when {
        jokers == 5 || cards.values.max() == 5 - jokers -> Type.FiveOfAKind
        cards.values.max() == 4 - jokers -> Type.FourOfAKind
        cards.size == 2 -> Type.FullHouse
        cards.values.max() == 3 - jokers -> Type.ThreeOfAKind
        cards.values.count { it == 2 } == 2 - jokers -> Type.TwoPair
        cards.values.count { it == 2 } == 1 - jokers -> Type.OnePair
        else -> Type.HighCard
    }
}

val comparator = Comparator<Hand> { a, b ->
    val aType = a.cards.type()
    val bType = b.cards.type()

    when {
        aType.ordinal > bType.ordinal -> 1
        aType.ordinal < bType.ordinal -> -1
        else -> {
            a.cards.indices.forEach { index ->
                when {
                    a.cards[index].ordinal > b.cards[index].ordinal -> return@Comparator 1
                    a.cards[index].ordinal < b.cards[index].ordinal -> return@Comparator -1
                }
            }

            0
        }
    }
}

data class Hand(val cards: List<Card>, val bid: Int)

fun part1() = input.sortedWith(comparator)
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

fun part2() = input.map { Hand(it.cards.map { if (it == Card.Jack) Card.Joker else it }, it.bid) }
    .sortedWith(comparator)
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}