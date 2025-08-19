import { useState, useEffect } from 'react'
import { Plus, Edit, Trash2, Bell, BellOff } from 'lucide-react'
import toast from 'react-hot-toast'

interface AlertRule {
  id: string
  symbol: string
  ruleType: string
  operator: string
  threshold: number
  enabled: boolean
  createdAt: string
}

export default function Alerts() {
  const [alerts, setAlerts] = useState<AlertRule[]>([])
  const [showForm, setShowForm] = useState(false)
  const [editingAlert, setEditingAlert] = useState<AlertRule | null>(null)
  const [formData, setFormData] = useState({
    symbol: '',
    ruleType: 'PRICE_THRESHOLD',
    operator: '>=',
    threshold: 0
  })

  // Mock data for demonstration
  useEffect(() => {
    const mockAlerts: AlertRule[] = [
      {
        id: '1',
        symbol: 'INFY',
        ruleType: 'PRICE_THRESHOLD',
        operator: '>=',
        threshold: 1800,
        enabled: true,
        createdAt: '2024-01-15T10:00:00Z'
      },
      {
        id: '2',
        symbol: 'TCS',
        ruleType: 'PRICE_THRESHOLD',
        operator: '<=',
        threshold: 3800,
        enabled: false,
        createdAt: '2024-01-14T15:30:00Z'
      }
    ]
    setAlerts(mockAlerts)
  }, [])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    
    if (editingAlert) {
      // Update existing alert
      setAlerts(alerts.map(alert => 
        alert.id === editingAlert.id 
          ? { ...alert, ...formData }
          : alert
      ))
      toast.success('Alert updated successfully')
    } else {
      // Create new alert
      const newAlert: AlertRule = {
        id: Date.now().toString(),
        ...formData,
        enabled: true,
        createdAt: new Date().toISOString()
      }
      setAlerts([...alerts, newAlert])
      toast.success('Alert created successfully')
    }
    
    setShowForm(false)
    setEditingAlert(null)
    setFormData({ symbol: '', ruleType: 'PRICE_THRESHOLD', operator: '>=', threshold: 0 })
  }

  const handleEdit = (alert: AlertRule) => {
    setEditingAlert(alert)
    setFormData({
      symbol: alert.symbol,
      ruleType: alert.ruleType,
      operator: alert.operator,
      threshold: alert.threshold
    })
    setShowForm(true)
  }

  const handleDelete = (id: string) => {
    setAlerts(alerts.filter(alert => alert.id !== id))
    toast.success('Alert deleted successfully')
  }

  const toggleAlert = (id: string) => {
    setAlerts(alerts.map(alert => 
      alert.id === id 
        ? { ...alert, enabled: !alert.enabled }
        : alert
    ))
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Alert Management</h2>
        <button
          onClick={() => setShowForm(true)}
          className="btn btn-primary flex items-center"
        >
          <Plus className="h-4 w-4 mr-2" />
          New Alert
        </button>
      </div>

      {/* Alert Form */}
      {showForm && (
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">
            {editingAlert ? 'Edit Alert' : 'Create New Alert'}
          </h3>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Symbol
                </label>
                <input
                  type="text"
                  value={formData.symbol}
                  onChange={(e) => setFormData({ ...formData, symbol: e.target.value })}
                  className="input"
                  placeholder="e.g., INFY"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Rule Type
                </label>
                <select
                  value={formData.ruleType}
                  onChange={(e) => setFormData({ ...formData, ruleType: e.target.value })}
                  className="input"
                >
                  <option value="PRICE_THRESHOLD">Price Threshold</option>
                  <option value="MA_CROSS">Moving Average Crossover</option>
                  <option value="PCT_CHANGE_WINDOW">Percentage Change</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Operator
                </label>
                <select
                  value={formData.operator}
                  onChange={(e) => setFormData({ ...formData, operator: e.target.value })}
                  className="input"
                >
                  <option value=">=">Greater than or equal to</option>
                  <option value="<=">Less than or equal to</option>
                  <option value=">">Greater than</option>
                  <option value="<">Less than</option>
                  <option value="==">Equal to</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Threshold
                </label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.threshold}
                  onChange={(e) => setFormData({ ...formData, threshold: parseFloat(e.target.value) })}
                  className="input"
                  placeholder="0.00"
                  required
                />
              </div>
            </div>
            <div className="flex justify-end space-x-2">
              <button
                type="button"
                onClick={() => {
                  setShowForm(false)
                  setEditingAlert(null)
                  setFormData({ symbol: '', ruleType: 'PRICE_THRESHOLD', operator: '>=', threshold: 0 })
                }}
                className="btn btn-secondary"
              >
                Cancel
              </button>
              <button type="submit" className="btn btn-primary">
                {editingAlert ? 'Update Alert' : 'Create Alert'}
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Alerts List */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">Active Alerts</h3>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Symbol
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Rule
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Created
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {alerts.map((alert) => (
                <tr key={alert.id}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <button
                      onClick={() => toggleAlert(alert.id)}
                      className={`p-2 rounded-full ${
                        alert.enabled 
                          ? 'text-success-600 hover:bg-success-50' 
                          : 'text-gray-400 hover:bg-gray-50'
                      }`}
                    >
                      {alert.enabled ? <Bell className="h-5 w-5" /> : <BellOff className="h-5 w-5" />}
                    </button>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {alert.symbol}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {alert.ruleType} {alert.operator} ₹{alert.threshold.toFixed(2)}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(alert.createdAt).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div className="flex space-x-2">
                      <button
                        onClick={() => handleEdit(alert)}
                        className="text-primary-600 hover:text-primary-900"
                      >
                        <Edit className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => handleDelete(alert.id)}
                        className="text-danger-600 hover:text-danger-900"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {alerts.length === 0 && (
          <div className="text-center py-8 text-gray-500">
            No alerts configured. Create your first alert to get started.
          </div>
        )}
      </div>
    </div>
  )
}
