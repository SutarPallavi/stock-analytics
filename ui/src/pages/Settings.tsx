import { useState } from 'react'
import { Save, RefreshCw, Database, Server, Bell } from 'lucide-react'
import toast from 'react-hot-toast'

export default function Settings() {
  const [settings, setSettings] = useState({
    refreshInterval: 1000,
    enableNotifications: true,
    enableSound: false,
    theme: 'light',
    language: 'en'
  })

  const [systemStatus, setSystemStatus] = useState({
    marketData: 'Connected',
    analytics: 'Connected',
    portfolio: 'Connected',
    alerts: 'Connected',
    database: 'Connected'
  })

  const handleSettingChange = (key: string, value: any) => {
    setSettings(prev => ({ ...prev, [key]: value }))
  }

  const handleSave = () => {
    // In a real app, this would save to backend
    toast.success('Settings saved successfully')
  }

  const checkSystemStatus = async () => {
    // In a real app, this would check actual service health
    toast.success('System status refreshed')
  }

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900">Settings</h2>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Application Settings */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">Application Settings</h3>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Data Refresh Interval (ms)
              </label>
              <input
                type="number"
                value={settings.refreshInterval}
                onChange={(e) => handleSettingChange('refreshInterval', parseInt(e.target.value))}
                className="input"
                min="500"
                max="10000"
                step="500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Theme
              </label>
              <select
                value={settings.theme}
                onChange={(e) => handleSettingChange('theme', e.target.value)}
                className="input"
              >
                <option value="light">Light</option>
                <option value="dark">Dark</option>
                <option value="auto">Auto</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Language
              </label>
              <select
                value={settings.language}
                onChange={(e) => handleSettingChange('language', e.target.value)}
                className="input"
              >
                <option value="en">English</option>
                <option value="es">Spanish</option>
                <option value="fr">French</option>
              </select>
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                id="enableNotifications"
                checked={settings.enableNotifications}
                onChange={(e) => handleSettingChange('enableNotifications', e.target.checked)}
                className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
              />
              <label htmlFor="enableNotifications" className="ml-2 block text-sm text-gray-900">
                Enable Notifications
              </label>
            </div>

            <div className="flex items-center">
              <input
                type="checkbox"
                id="enableSound"
                checked={settings.enableSound}
                onChange={(e) => handleSettingChange('enableSound', e.target.checked)}
                className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
              />
              <label htmlFor="enableSound" className="ml-2 block text-sm text-gray-900">
                Enable Sound Alerts
              </label>
            </div>

            <button
              onClick={handleSave}
              className="btn btn-primary flex items-center"
            >
              <Save className="h-4 w-4 mr-2" />
              Save Settings
            </button>
          </div>
        </div>

        {/* System Status */}
        <div className="card">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-semibold text-gray-900">System Status</h3>
            <button
              onClick={checkSystemStatus}
              className="btn btn-secondary flex items-center"
            >
              <RefreshCw className="h-4 w-4 mr-2" />
              Refresh
            </button>
          </div>
          
          <div className="space-y-3">
            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <Server className="h-5 w-5 text-gray-400 mr-3" />
                <span className="text-sm font-medium text-gray-900">Market Data Service</span>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                systemStatus.marketData === 'Connected' 
                  ? 'bg-success-100 text-success-800' 
                  : 'bg-danger-100 text-danger-800'
              }`}>
                {systemStatus.marketData}
              </span>
            </div>

            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <Database className="h-5 w-5 text-gray-400 mr-3" />
                <span className="text-sm font-medium text-gray-900">Analytics Service</span>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                systemStatus.analytics === 'Connected' 
                  ? 'bg-success-100 text-success-800' 
                  : 'bg-danger-100 text-danger-800'
              }`}>
                {systemStatus.analytics}
              </span>
            </div>

            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <Database className="h-5 w-5 text-gray-400 mr-3" />
                <span className="text-sm font-medium text-gray-900">Portfolio Service</span>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                systemStatus.portfolio === 'Connected' 
                  ? 'bg-success-100 text-success-800' 
                  : 'bg-danger-100 text-danger-800'
              }`}>
                {systemStatus.portfolio}
              </span>
            </div>

            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <Bell className="h-5 w-5 text-gray-400 mr-3" />
                <span className="text-sm font-medium text-gray-900">Alerts Service</span>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                systemStatus.alerts === 'Connected' 
                  ? 'bg-success-100 text-success-800' 
                  : 'bg-danger-100 text-danger-800'
              }`}>
                {systemStatus.alerts}
              </span>
            </div>

            <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div className="flex items-center">
                <Database className="h-5 w-5 text-gray-400 mr-3" />
                <span className="text-sm font-medium text-gray-900">Database</span>
              </div>
              <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                systemStatus.database === 'Connected' 
                  ? 'bg-success-100 text-success-800' 
                  : 'bg-danger-100 text-danger-800'
              }`}>
                {systemStatus.database}
              </span>
            </div>
          </div>
        </div>
      </div>

      {/* About */}
      <div className="card">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">About</h3>
        <div className="space-y-2 text-sm text-gray-600">
          <p><strong>Version:</strong> 1.0.0</p>
          <p><strong>Build Date:</strong> {new Date().toLocaleDateString()}</p>
          <p><strong>Environment:</strong> Development</p>
          <p><strong>API Base URL:</strong> http://localhost:8080</p>
        </div>
      </div>
    </div>
  )
}
