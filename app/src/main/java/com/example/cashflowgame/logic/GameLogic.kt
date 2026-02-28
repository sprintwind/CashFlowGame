package com.example.cashflowgame.logic

import com.example.cashflowgame.model.CardType
import com.example.cashflowgame.model.GameCard

class GameLogic {

    private val opportunities = listOf(
        GameCard(1, CardType.OPPORTUNITY, "小型投资", "投资一个小项目", assetCost = 5000, assetReturn = 0.15f),
        GameCard(2, CardType.OPPORTUNITY, "股票投资", "购买潜力股票", assetCost = 10000, assetReturn = 0.12f),
        GameCard(3, CardType.OPPORTUNITY, "出租房产", "购买出租房", assetCost = 50000, assetReturn = 0.08f),
        GameCard(4, CardType.OPPORTUNITY, "便利店加盟", "加盟便利店", assetCost = 30000, assetReturn = 0.10f),
        GameCard(5, CardType.OPPORTUNITY, "网上店铺", "开网店", assetCost = 8000, assetReturn = 0.20f),
        GameCard(6, CardType.OPPORTUNITY, "基金理财", "购买基金", assetCost = 20000, assetReturn = 0.07f)
    )

    private val markets = listOf(
        GameCard(10, CardType.MARKET, "市场繁荣", "经济形势良好", amount = 1000),
        GameCard(11, CardType.MARKET, "股票上涨", "持有的股票升值了", amount = 2000),
        GameCard(12, CardType.MARKET, "房产增值", "房产价值上升", amount = 5000),
        GameCard(13, CardType.MARKET, "投资收益", "投资获得分红", amount = 1500),
        GameCard(14, CardType.MARKET, "经济衰退", "市场不景气", amount = -1000),
        GameCard(15, CardType.MARKET, "股票下跌", "股票亏损", amount = -2000)
    )

    private val charity = listOf(
        GameCard(20, CardType.CHARITY, "慈善捐款", "向慈善机构捐款", amount = 1000, incomeChange = 200),
        GameCard(21, CardType.CHARITY, "公益活动", "参加公益活动", amount = 500, incomeChange = 100)
    )

    private val babies = listOf(
        GameCard(30, CardType.BABY, "生孩子", "恭喜你有了孩子!", expenseChange = 1000),
        GameCard(31, CardType.BABY, "第二个孩子", "家庭新成员", expenseChange = 1500)
    )

    private val downsizes = listOf(
        GameCard(40, CardType.DOWNSIZE, "失业!", "被裁员了", amount = 2000, incomeChange = -2000),
        GameCard(41, CardType.DOWNSIZE, "创业", "决定创业", amount = 5000, incomeChange = 1000)
    )

    private val careers = listOf(
        GameCard(50, CardType.CAREER, "医生", "月入过万", incomeChange = 15000, expenseChange = 5000, amount = 10000),
        GameCard(51, CardType.CAREER, "工程师", "技术工作", incomeChange = 10000, expenseChange = 4000, amount = 8000),
        GameCard(52, CardType.CAREER, "销售", "业绩提成", incomeChange = 8000, expenseChange = 3500, amount = 5000),
        GameCard(53, CardType.CAREER, "教师", "稳定工作", incomeChange = 6000, expenseChange = 3000, amount = 5000),
        GameCard(54, CardType.CAREER, "服务员", "基层工作", incomeChange = 4000, expenseChange = 2500, amount = 2000)
    )

    fun drawCard(position: Int): GameCard {
        return when (position) {
            0 -> GameCard(0, CardType.CAREER, "起点", "每月获得工资")
            1, 3, 7, 11, 13, 17 -> opportunities.random()
            2, 6, 9, 12, 16 -> markets.random()
            4, 14 -> charity.random()
            8, 18 -> babies.random()
            10 -> downsizes.random()
            5, 15 -> GameCard(99, CardType.OPPORTUNITY, "大机会", "优质投资项目", assetCost = 100000, assetReturn = 0.12f)
            else -> GameCard(0, CardType.CAREER, "待命", "等待机会")
        }
    }
}
