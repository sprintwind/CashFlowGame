package com.example.cashflowgame.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cashflowgame.R
import com.example.cashflowgame.logic.GameLogic
import com.example.cashflowgame.model.CardType
import com.example.cashflowgame.model.GameCard
import com.example.cashflowgame.model.Player
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var player: Player
    private lateinit var gameLogic: GameLogic
    
    private lateinit var tvCash: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpenses: TextView
    private lateinit var tvPassiveIncome: TextView
    private lateinit var tvCashFlow: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvCardTitle: TextView
    private lateinit var tvCardDescription: TextView
    private lateinit var gridBoard: GridLayout
    private lateinit var btnRollDice: Button
    private lateinit var btnNextTurn: Button
    private lateinit var diceContainer: LinearLayout
    private lateinit var tvDice1: TextView
    private lateinit var tvDice2: TextView
    
    private val boardCells = mutableListOf<TextView>()
    private var hasRolledThisTurn = false
    private val diceValues = arrayOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        player = Player()
        gameLogic = GameLogic()
        
        initViews()
        initBoard()
        updateUI()
    }

    private fun initViews() {
        tvCash = findViewById(R.id.tvCash)
        tvIncome = findViewById(R.id.tvIncome)
        tvExpenses = findViewById(R.id.tvExpenses)
        tvPassiveIncome = findViewById(R.id.tvPassiveIncome)
        tvCashFlow = findViewById(R.id.tvCashFlow)
        tvProgress = findViewById(R.id.tvProgress)
        tvCardTitle = findViewById(R.id.tvCardTitle)
        tvCardDescription = findViewById(R.id.tvCardDescription)
        gridBoard = findViewById(R.id.gridBoard)
        btnRollDice = findViewById(R.id.btnRollDice)
        btnNextTurn = findViewById(R.id.btnNextTurn)
        diceContainer = findViewById(R.id.diceContainer)
        tvDice1 = findViewById(R.id.tvDice1)
        tvDice2 = findViewById(R.id.tvDice2)
        
        btnRollDice.setOnClickListener { rollDice() }
        btnNextTurn.setOnClickListener { nextTurn() }
    }

    private fun initBoard() {
        val boardSize = 20
        val cellSize = resources.displayMetrics.widthPixels / 5 - 32
        
        for (i in 0 until boardSize) {
            val cell = TextView(this).apply {
                text = getBoardLabel(i)
                textSize = 10f
                gravity = Gravity.CENTER
                setBackgroundColor(getCellColor(i))
                layoutParams = GridLayout.LayoutParams().apply {
                    width = cellSize
                    height = cellSize
                    setMargins(4, 4, 4, 4)
                }
            }
            boardCells.add(cell)
            gridBoard.addView(cell)
        }
        updateBoardPosition()
    }

    private fun getBoardLabel(position: Int): String {
        return when (position) {
            0 -> "起点\n工资"
            1 -> "机会"
            2 -> "市场"
            3 -> "机会"
            4 -> "慈善"
            5 -> "小生意"
            6 -> "市场"
            7 -> "机会"
            8 -> "孩子"
            9 -> "市场"
            10 -> "失业\n-5000"
            11 -> "机会"
            12 -> "市场"
            13 -> "小生意"
            14 -> "慈善"
            15 -> "大生意"
            16 -> "市场"
            17 -> "机会"
            18 -> "孩子"
            19 -> "大生意"
            else -> ""
        }
    }

    private fun getCellColor(position: Int): Int {
        return when (position) {
            0 -> 0xFF4CAF50.toInt()
            5, 15 -> 0xFFFF9800.toInt()
            10 -> 0xFFF44336.toInt()
            else -> 0xFFFFFFFF.toInt()
        }
    }

    private fun rollDice() {
        if (hasRolledThisTurn) return
        
        btnRollDice.isEnabled = false
        gridBoard.visibility = View.INVISIBLE
        diceContainer.visibility = View.VISIBLE
        
        val dice1 = Random.nextInt(1, 7)
        val dice2 = Random.nextInt(1, 7)
        val total = dice1 + dice2
        
        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.dice_shake)
        
        var counter = 0
        val updateRunnable = object : Runnable {
            override fun run() {
                if (counter < 10) {
                    tvDice1.text = diceValues[Random.nextInt(6)]
                    tvDice2.text = diceValues[Random.nextInt(6)]
                    counter++
                    handler.postDelayed(this, 80)
                } else {
                    tvDice1.text = diceValues[dice1 - 1]
                    tvDice2.text = diceValues[dice2 - 1]
                    
                    handler.postDelayed({
                        diceContainer.visibility = View.INVISIBLE
                        gridBoard.visibility = View.VISIBLE
                        
                        Toast.makeText(this@MainActivity, "掷出: $dice1 + $dice2 = $total", Toast.LENGTH_SHORT).show()
                        
                        player.position = (player.position + total) % 20
                        
                        updateBoardPosition()
                        processCurrentTile()
                        
                        hasRolledThisTurn = true
                        btnRollDice.isEnabled = true
                        btnNextTurn.visibility = View.VISIBLE
                    }, 300)
                }
            }
        }
        
        tvDice1.startAnimation(shakeAnim)
        tvDice2.startAnimation(shakeAnim)
        handler.post(updateRunnable)
    }

    private fun updateBoardPosition() {
        for (i in boardCells.indices) {
            boardCells[i].setBackgroundColor(getCellColor(i))
            if (i == player.position) {
                boardCells[i].setBackgroundColor(0xFF2196F3.toInt())
            }
        }
    }

    private fun processCurrentTile() {
        val card = gameLogic.drawCard(player.position)
        showCard(card)
        applyCardEffect(card)
        updateUI()
        checkWinCondition()
    }

    private fun showCard(card: GameCard) {
        tvCardTitle.text = card.title
        tvCardDescription.text = card.description
    }

    private fun applyCardEffect(card: GameCard) {
        when (card.type) {
            CardType.OPPORTUNITY -> {
                if (card.assetCost > 0 && player.cash >= card.assetCost) {
                    showBuyDialog(card)
                }
            }
            CardType.MARKET -> {
                player.cash += card.amount
            }
            CardType.CHARITY -> {
                if (card.amount > 0 && player.cash >= card.amount) {
                    player.cash -= card.amount
                    player.passiveIncome += card.incomeChange
                }
            }
            CardType.BABY -> {
                player.monthlyExpenses += card.expenseChange
            }
            CardType.DOWNSIZE -> {
                player.cash += card.amount
                player.monthlyIncome = maxOf(2000, player.monthlyIncome + card.incomeChange)
            }
            CardType.CAREER -> {
                player.monthlyIncome = card.incomeChange
                player.monthlyExpenses = card.expenseChange
                player.cash += card.amount
            }
        }
    }

    private fun showBuyDialog(card: GameCard) {
        AlertDialog.Builder(this)
            .setTitle(card.title)
            .setMessage("${card.description}\n\n成本: ${card.assetCost}元\n预期回报: ${(card.assetReturn * 100).toInt()}%")
            .setPositiveButton("购买") { _, _ ->
                player.cash -= card.assetCost
                val monthlyReturn = (card.assetCost * card.assetReturn / 12).toInt()
                player.passiveIncome += monthlyReturn
                player.totalAssets += card.assetCost
                Toast.makeText(this, "购买成功! 每月增加被动收入 $monthlyReturn 元", Toast.LENGTH_SHORT).show()
                updateUI()
            }
            .setNegativeButton("跳过", null)
            .show()
    }

    private fun nextTurn() {
        player.cash += player.cashFlow
        
        hasRolledThisTurn = false
        btnRollDice.isEnabled = true
        btnNextTurn.visibility = android.view.View.GONE
        
        Toast.makeText(this, "新回合开始!", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI() {
        tvCash.text = player.cash.toString()
        tvIncome.text = player.monthlyIncome.toString()
        tvExpenses.text = player.monthlyExpenses.toString()
        tvPassiveIncome.text = player.passiveIncome.toString()
        tvCashFlow.text = player.cashFlow.toString()
        tvProgress.text = "${player.passiveIncome} / ${player.monthlyExpenses}"
    }

    private fun checkWinCondition() {
        if (player.isRich) {
            AlertDialog.Builder(this)
                .setTitle("恭喜!")
                .setMessage("你已经跳出老鼠赛跑!\n\n被动收入: ${player.passiveIncome}元\n月支出: ${player.monthlyExpenses}元\n\n你赢了!")
                .setPositiveButton("再玩一次") { _, _ ->
                    player = Player()
                    updateUI()
                    updateBoardPosition()
                    hasRolledThisTurn = false
                    btnRollDice.isEnabled = true
                    btnNextTurn.visibility = android.view.View.GONE
                }
                .setCancelable(false)
                .show()
        }
    }
}
