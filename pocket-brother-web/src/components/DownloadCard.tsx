import { Download, Smartphone, ShieldCheck, HardDrive, Cpu, Layers, HelpCircle } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

interface DownloadCardProps {
  onOpenBuildGuide?: () => void;
}

export const DownloadCard = ({ onOpenBuildGuide }: DownloadCardProps) => {
  return (
    <div className="container" style={{ marginBottom: '48px' }}>
      <div className="download-spec-card">
        {/* Amber accent line */}
        <div style={{
          position: 'absolute',
          top: 0,
          left: 0,
          right: 0,
          height: '2px',
          background: 'linear-gradient(90deg, transparent 0%, var(--accent-amber) 50%, transparent 100%)',
        }} />

        <div className="download-card-body">
          {/* Top Status */}
          <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            flexWrap: 'wrap',
            gap: '8px',
            marginBottom: '16px',
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <span className="pill-badge" style={{ fontSize: '0.75rem', padding: '3px 10px' }}>
                <ShieldCheck size={13} color="#34D399" />
                Verified APK Build
              </span>
              <span style={{ fontSize: '0.8rem', color: 'var(--text-subtle)' }}>
                Target SDK 36
              </span>
            </div>
            
            <div style={{ fontSize: '0.78rem', color: 'var(--accent-amber)', fontWeight: 600 }}>
              ● Standalone Sideload
            </div>
          </div>

          {/* Quick Metrics Ribbon (App Store style for mobile) */}
          <div className="spec-metrics-grid">
            <div className="metric-item">
              <div className="metric-label">
                <HardDrive size={11} />
                <span>Size</span>
              </div>
              <div className="metric-val" style={{ color: 'var(--accent-amber)' }}>
                {APP_CONFIG.apkEstimatedSize}
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Layers size={11} />
                <span>Version</span>
              </div>
              <div className="metric-val">
                v{APP_CONFIG.version}
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Smartphone size={11} />
                <span>OS</span>
              </div>
              <div className="metric-val">
                Android 7+
              </div>
            </div>

            <div className="metric-item">
              <div className="metric-label">
                <Cpu size={11} />
                <span>Engine</span>
              </div>
              <div className="metric-val">
                On-Device
              </div>
            </div>
          </div>

          {/* CTA & Fallback direct link */}
          <div style={{ marginTop: '20px' }}>
            <a
              href={APP_CONFIG.apkDownloadPath}
              download={APP_CONFIG.apkFileName}
              className="btn-primary"
              id="download-card-btn"
              style={{
                width: '100%',
                justifyContent: 'center',
                padding: '14px 20px',
                fontSize: '0.975rem',
              }}
            >
              <Download size={18} />
              <span>Download APK ({APP_CONFIG.apkEstimatedSize})</span>
            </a>

            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              flexWrap: 'wrap',
              gap: '6px',
              fontSize: '0.8rem',
              color: 'var(--text-subtle)',
              marginTop: '10px',
            }}>
              <span>Direct package link: </span>
              <a
                href={APP_CONFIG.apkDownloadPath}
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: 'var(--accent-amber)', textDecoration: 'underline' }}
              >
                {APP_CONFIG.apkFileName}
              </a>
              {onOpenBuildGuide && (
                <>
                  <span> • </span>
                  <button
                    onClick={onOpenBuildGuide}
                    style={{
                      color: 'var(--text-muted)',
                      textDecoration: 'underline',
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '4px',
                      fontSize: 'inherit',
                    }}
                  >
                    <HelpCircle size={12} />
                    <span>How it works</span>
                  </button>
                </>
              )}
            </div>
          </div>

        </div>
      </div>

      <style>{`
        .download-spec-card {
          background: linear-gradient(135deg, #1F1916 0%, #261F1B 100%);
          border: 1px solid var(--border-medium);
          border-radius: var(--radius-lg);
          padding: 20px 18px;
          position: relative;
          overflow: hidden;
          box-shadow: var(--shadow-card);
        }

        .spec-metrics-grid {
          display: grid;
          grid-template-columns: repeat(4, 1fr);
          gap: 8px;
          background: rgba(18, 14, 12, 0.6);
          border: 1px solid var(--border-subtle);
          border-radius: var(--radius-md);
          padding: 12px 10px;
        }

        @media (max-width: 480px) {
          .spec-metrics-grid {
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
          }
        }

        .metric-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          text-align: center;
        }

        .metric-label {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 0.72rem;
          color: var(--text-subtle);
          text-transform: uppercase;
          letter-spacing: 0.04em;
        }

        .metric-val {
          font-size: 0.925rem;
          font-weight: 700;
          color: var(--text-cream);
          margin-top: 2px;
        }

        @media (min-width: 768px) {
          .download-spec-card {
            padding: 26px 28px;
            border-radius: var(--radius-xl);
          }
        }
      `}</style>
    </div>
  );
};
