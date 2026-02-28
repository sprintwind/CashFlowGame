package com.example.cashflowgame.model

data class Player(
    var name: String = "玩家",
    var cash: Int = 10000,
    var monthlyIncome: Int = 5000,
    var monthlyExpenses: Int = 4000,
    var passiveIncome: Int = 0,
    var totalAssets: Int = 0,
    var totalLiabilities: Int = 0,
    var position: Int = 0
) {
    val cashFlow: Int get() = monthlyIncome - monthlyExpenses + passiveIncome
    
    val isRich: Boolean get() = passiveIncome > monthlyExpenses
}

enum class CardType {
    CAREER,      // 职业卡
    OPPORTUNITY,  // 投资机会
    MARKET,      // 市场事件
    CHARITY,     // 慈善
    BABY,        // 生孩子
    DOWNSIZE     // 破产/裁员
}

data class GameCard(
    val id: Int,
    val type: CardType,
    val title: String,
    val description: String,
    val amount: Int = 0,
    val incomeChange: Int = 0,
    val expenseChange: Int = 0,
    val assetReturn: Float = 0f,
    val assetCost: Int = 0
)

data class Career(
    val title: String,
    val monthlyIncome: Int,
    val monthlyExpenses: Int,
    val initialSavings: Int
)
