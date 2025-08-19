import { useState, useEffect } from 'react'
import { Play, Square, TrendingUp, TrendingDown } from 'lucide-react'
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts'
import toast from 'react-hot-toast'

interface StockTick {
  symbol: string
  lastPrice: number
  timestamp: string
  volume: number
  change: number
  changePercent: number
}

interface AnalyticsMetric {
  symbol: string
  sma20: number
  ema12: number
  vwap5m: number
  timestamp: string
}

export default function Dashboard() {
  const [isGenerating, setIsGenerating] = useState(false)
  const [ticks, setTicks] = useState<StockTick[]>([])
  const [metrics, setMetrics] = useState<AnalyticsMetric[]>([])

  const symbols = ['INFY', 'TCS', 'RELIANCE', 'HDFC', 'ICICIBANK']

  const startTickGeneration = async () => {
    try {
      const response = await fetch('/api/v1/generator/start?symbols=INFY,TCS,RELIANCE&rate=50', {
        method: 'POST'
      })
      if (response.ok) {
        setIsGenerating(true)
        toast.success('Tick generation started')
      }
    } catch (error) {
      toast.error('Failed to start tick generation')
    }
  }

  const stopTickGeneration = async () => {
    try {
      const response = await fetch('/api/v1/generator/stop', {
        method: 'POST'
      })
      if (response.ok) {
        setIsGenerating(false)
        toast.success('Tick generation stopped')
      }
    } catch (error) {
      toast.error('Failed to stop tick generation')
    }
  }

  // Mock data for demonstration
  useEffect(() => {
    const mockTicks: StockTick[] = symbols.map(symbol => ({
      symbol,
      lastPrice: Math.random() * 1000 + 500,
      timestamp: new Date().toISOString(),
      volume: Math.floor(Math.random() * 1000000) + 100000,
      change: (Math.random() - 0.5) * 20,
      changePercent: (Math.random() - 0.5) * 5
    }))

    const mockMetrics: AnalyticsMetric[] = symbols.map(symbol => ({
      symbol,
      sma20: Math.random() * 1000 + 500,
      ema12: Math.random() * 1000 + 500,
      vwap5m: Math.random() * 1000 + 500,
      timestamp: new Date().toISOString()
    }))

    setTicks(mockTicks)
    setMetrics(mockMetrics)
  }, [])

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Market Dashboard</h2>
        <div className="flex space-x-2">
          {!isGenerating ? (
            <button
              onClick={startTickGeneration}
              className="btn btn-primary flex items-center"
            >
              <Play className="h-4 w-4 mr-2" />
              Start Ticks
            </button>
          ) : (
            <button
              onClick={stopTickGeneration}
              className="btn btn-secondary flex items-center"
            >
              <Square className="h-4 w-4 mr-2" />
              Stop Ticks
            </button>
          )}
        </div>
      </div>

      {/* Live Ticks */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {ticks.map((tick) => (
          <div key={tick.symbol} className="card">
            <div className="flex justify-between items-start mb-4">
              <h3 className="text-lg font-semibold text-gray-900">{tick.symbol}</h3>
              <div className="text-right">
                <div className="text-2xl font-bold text-gray-900">
                  ₹{tick.lastPrice.toFixed(2)}
                </div>
                <div className={`flex items-center text-sm ${
                  tick.change >= 0 ? 'text-success-600' : 'text-danger-600'
                }`}>
                  {tick.change >= 0 ? (
                    <TrendingUp className="h-4 w-4 mr-1" />
                  ) : (
                    <TrendingDown className="h-4 w-4 mr-1" />
                  )}
                  {tick.change.toFixed(2)} ({tick.changePercent.toFixed(2)}%)
                </div>
              </div>
            </div>
            <div className="text-sm text-gray-500">
              Volume: {tick.volume.toLocaleString()}
            </div>
          </div>
        ))}
      </div>

      {/* Analytics Chart */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Price Trends</h3>
        <div className="h-80">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={ticks}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="symbol" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="lastPrice" stroke="#3b82f6" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Technical Indicators */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Technical Indicators</h3>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Symbol
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  SMA(20)
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  EMA(12)
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  VWAP(5m)
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {metrics.map((metric) => (
                <tr key={metric.symbol}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {metric.symbol}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ₹{metric.sma20.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ₹{metric.ema12.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ₹{metric.vwap5m.toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
