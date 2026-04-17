package model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RankTest{

    @Test
    fun `every rank equals its value`(){
        val ranks: List<Rank> = Rank.entries.sortedBy {it.value}
        ranks.forEachIndexed {index:Int, rank ->
            val expectedValue: Int = index + 1
            assertEquals(expectedValue, rank.value)
        }
    }

    @Test
    fun `every rank has its right character`(){
        Rank.entries.forEach {rank->
            val expected = when(rank){
                Rank.ACE->"A"
                Rank.JACK->"J"
                Rank.QUEEN->"Q"
                Rank.KING->"K"
                else-> rank.value.toString()
            }
            assertEquals(expected, rank.displaySymbol())
        }
    }

    @Test
    fun `KING has the highest value and the ACE the lowest value`(){
        assertEquals(1,Rank.ACE.value)
        assertEquals(13, Rank.KING.value)
    }
}