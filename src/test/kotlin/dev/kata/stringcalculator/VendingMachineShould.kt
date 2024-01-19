package dev.kata.stringcalculator

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.OptionalDouble

typealias CoinValidity = OptionalDouble

internal class VendingMachineShould {


        @Test
        fun `display 'INSERT COIN' message when there is no purchase in progress`() {
            val vendingMachine = VendingMachine()

            assertThat(vendingMachine.screenMessage()).isEqualTo("INSERT COIN")
        }

        @Test
        fun `reject invalid coins and place them in the coin return`() {
            val vendingMachine = VendingMachine()
            val insertedCoin = Coin.Pennie

            vendingMachine.insert(insertedCoin)

            assertThat(vendingMachine.screenMessage()).isEqualTo("INSERT COIN")
            assertThat(vendingMachine.coinReturn()).isEqualTo(listOf(insertedCoin))
        }

        @Test
        fun `display the collected amount when inserting valid coins`() {
            val vendingMachine = VendingMachine()

            vendingMachine.insert(Coin.Nickel)
            vendingMachine.insert(Coin.Dime)
            vendingMachine.insert(Coin.Quarter)

            assertThat(vendingMachine.screenMessage()).isEqualTo("INSERT COIN")
            assertThat(vendingMachine.screenAmount()).isEqualTo("$0.40")
        }

}

class VendingMachine {
    private val coinReturn = mutableListOf<Coin>()
    private var collectedAmount = 0.0

    fun screenMessage(): String {
        return "INSERT COIN";
    }

    fun insert(coin: Coin) {
        identifyAmountFrom(coin)
            .ifPresentOrElse(
                { collectedAmount += it },
                { coinReturn.add(coin) }
            )
    }

    private fun identifyAmountFrom(coin: Coin): CoinValidity {
        return when (coin) {
            Coin.Pennie -> CoinValidity.empty()
            Coin.Nickel -> CoinValidity.of(0.05)
            Coin.Dime -> CoinValidity.of(0.10)
            Coin.Quarter -> CoinValidity.of(0.25)
        }
    }

    fun screenAmount(): String {
        return collectedAmount.toMoneyDisplayFormat()
    }

    fun coinReturn(): MutableList<Coin> {
        return coinReturn
    }

}

fun Double.toMoneyDisplayFormat(): String {
    return String.format("\$%.2f", this).replace(",", ".")
}
