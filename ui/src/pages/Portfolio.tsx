import { useState, useEffect } from 'react'
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts'

interface Position {
  symbol: string
  quantity: number
  averagePrice: number
  currentPrice: number
  marketValue: number
  unrealizedPnL: number
  unrealizedPnLPercent: number
}

interface Portfolio {
  cash: number
  positions: Position[]
  totalValue: number
  totalPnL: number
  totalPnLPercent: number
}

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6']

export default function Portfolio() {
  const [portfolio, setPortfolio] = useState<Portfolio | null>(null)
  const [loading, setLoading] = useState(true)

  // Mock data for demonstration
  useEffect(() => {
    const mockPortfolio: Portfolio = {
      cash: 50000,
      positions: [
        {
          symbol: 'INFY',
          quantity: 100,
          averagePrice: 1750,
          currentPrice: 1800,
          marketValue: 180000,
          unrealizedPnL: 5000,
          unrealizedPnLPercent: 2.86
        },
        {
          symbol: 'TCS',
          quantity: 50,
          averagePrice: 3800,
          currentPrice: 4000,
          marketValue: 200000,
          unrealizedPnL: 10000,
          unrealizedPnLPercent: 5.26
        },
        {
          symbol: 'RELIANCE',
          quantity: 75,
          averagePrice: 2400,
          currentPrice: 2500,
          marketValue: 187500,
          unrealizedPnL: 7500,
          unrealizedPnLPercent: 4.17
        }
      ],
      totalValue: 0,
      totalPnL: 0,
      totalPnLPercent: 0
    }

    // Calculate totals
    const totalMarketValue = mockPortfolio.positions.reduce((sum, pos) => sum + pos.marketValue, 0)
    const totalPnL = mockPortfolio.positions.reduce((sum, pos) => sum + pos.unrealizedPnL, 0)
    const totalPnLPercent = (totalPnL / (totalMarketValue - totalPnL)) * 100

    mockPortfolio.totalValue = mockPortfolio.cash + totalMarketValue
    mockPortfolio.totalPnL = totalPnL
    mockPortfolio.totalPnLPercent = totalPnLPercent

    setPortfolio(mockPortfolio)
    setLoading(false)
  }, [])

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-gray-500">Loading portfolio...</div>
      </div>
    )
  }

  if (!portfolio) {
    return (
      <div className="text-center text-gray-500">
        Failed to load portfolio
      </div>
    )
  }

  const chartData = portfolio.positions.map(pos => ({
    name: pos.symbol,
    value: pos.marketValue
  }))

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Portfolio Overview</h2>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">Total Value</h3>
          <p className="text-2xl font-bold text-gray-900">₹{portfolio.totalValue.toLocaleString()}</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">Cash</h3>
          <p className="text-2xl font-bold text-gray-900">₹{portfolio.cash.toLocaleString()}</p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">Unrealized P&L</h3>
          <p className={`text-2xl font-bold ${portfolio.totalPnL >= 0 ? 'text-success-600' : 'text-danger-600'}`}>
            ₹{portfolio.totalPnL.toLocaleString()}
          </p>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-500 mb-2">P&L %</h3>
          <p className={`text-2xl font-bold ${portfolio.totalPnLPercent >= 0 ? 'text-success-600' : 'text-danger-600'}`}>
            {portfolio.totalPnLPercent.toFixed(2)}%
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Portfolio Allocation Chart */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Portfolio Allocation</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={chartData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Positions Table */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Positions</h3>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Symbol
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Qty
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Avg Price
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Market Value
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    P&L
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {portfolio.positions.map((position) => (
                  <tr key={position.symbol}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {position.symbol}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {position.quantity}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      ₹{position.averagePrice.toFixed(2)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      ₹{position.marketValue.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <span className={position.unrealizedPnL >= 0 ? 'text-success-600' : 'text-danger-600'}>
                        ₹{position.unrealizedPnL.toLocaleString()} ({position.unrealizedPnLPercent.toFixed(2)}%)
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}
